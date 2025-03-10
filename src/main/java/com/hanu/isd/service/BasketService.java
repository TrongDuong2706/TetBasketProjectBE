package com.hanu.isd.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.hanu.isd.dto.request.BasketRequest;
import com.hanu.isd.dto.response.BasketResponse;
import com.hanu.isd.dto.response.PaginatedResponse;
import com.hanu.isd.entity.*;
import com.hanu.isd.exception.AppException;
import com.hanu.isd.exception.ErrorCode;
import com.hanu.isd.mapper.BasketMapper;
import com.hanu.isd.repository.BasketCategoryRepository;
import com.hanu.isd.repository.BasketImageRepository;
import com.hanu.isd.repository.BasketRepository;
import com.hanu.isd.repository.ItemRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BasketService {
    BasketMapper basketMapper;
    BasketRepository basketRepository;
    Cloudinary cloudinary;
    BasketCategoryRepository basketCategoryRepository;
    BasketImageRepository basketImageRepository;
    ItemRepository itemRepository;

    public BasketResponse createBasket(BasketRequest request, List<MultipartFile> files) {
        // Tạo đối tượng Basket từ BasketRequest
        Basket basket = basketMapper.toBasket(request);

        // Lưu giỏ hàng để đảm bảo có ID trước khi liên kết các thực thể liên quan
        basket = basketRepository.save(basket);

        // Xử lý các hình ảnh (nếu có)
        Set<BasketImage> basketImages = new HashSet<>();
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                try {
                    // Tải ảnh lên Cloudinary
                    Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
                    String url = uploadResult.get("url").toString();

                    // Tạo đối tượng BasketImage
                    BasketImage image = BasketImage.builder()
                            .imageUrl(url)
                            .basket(basket)
                            .build();
                    basketImages.add(image);
                } catch (IOException e) {
                    log.error("Error uploading image to Cloudinary", e);
                }
            }
        }
        // Gán tập hợp ảnh vào giỏ hàng
        basket.setImages(basketImages);
        // Tạo và liên kết các mục với giỏ hàng
        Set<Item> items = new HashSet<>();
        for (String itemName : request.getItemNames()) {
            Item item = Item.builder()
                    .name(itemName)
                    .basket(basket)
                    .build();
            items.add(item);
        }
        basket.setItems(items);

        // Lưu giỏ hàng cùng các quan hệ liên quan
        basket = basketRepository.save(basket);
        // Chuyển đổi sang BasketResponse để trả về
        return basketMapper.toBasketResponse(basket);
    }



    //Get All Basket
    public PaginatedResponse<BasketResponse> getAllBasket(int page, int size){
        PageRequest pageRequest = PageRequest.of(page,size);
        Page<Basket> basket = basketRepository.findAll(pageRequest);
        List<BasketResponse> basketResponses = basket.getContent().stream().map(basketMapper::toBasketResponse).toList();
        return PaginatedResponse.<BasketResponse>builder()
                .totalItems((int)(basket.getTotalElements()))
                .totalPages(basket.getTotalPages())
                .currentPage(basket.getNumber())
                .pageSize(basket.getSize())
                .hasNextPage(basket.hasNext())
                .hasPreviousPage(basket.hasPrevious())
                .elements(basketResponses)
                .build();
    }

    //Update Hotel
    public BasketResponse updateBasket(BasketRequest basketRequest, List<MultipartFile> files, Long basketId, boolean keepExistingImages) {
        // Lấy giỏ hàng từ cơ sở dữ liệu
        Basket basket = basketRepository.findById(basketId)
                .orElseThrow(() -> new AppException(ErrorCode.BASKET_NOT_EXISTED));

        // Cập nhật thông tin giỏ hàng
        basketMapper.updateBasket(basket, basketRequest);

        // Nếu có ảnh mới thì cập nhật, nếu không thì giữ ảnh cũ
        if (files != null && !files.isEmpty()) {
            // Xóa ảnh cũ trên Cloudinary nếu không giữ lại ảnh cũ
            if (!keepExistingImages) {
                for (BasketImage oldImage : basket.getImages()) {
                    try {
                        String publicId = oldImage.getImageUrl().substring(oldImage.getImageUrl().lastIndexOf("/") + 1, oldImage.getImageUrl().lastIndexOf("."));
                        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                    } catch (IOException e) {
                        log.error("Error deleting image from Cloudinary", e);
                    }
                }

                // Xóa tất cả các ảnh cũ khỏi cơ sở dữ liệu
                basketImageRepository.deleteAll(basket.getImages());
                basket.getImages().clear();
            }

            // Tạo một tập hợp để lưu các đối tượng hình ảnh mới
            Set<BasketImage> newImages = new HashSet<>();
            for (MultipartFile file : files) {
                try {
                    // Tải hình ảnh lên Cloudinary
                    Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
                    String url = uploadResult.get("url").toString();

                    // Tạo và thiết lập đối tượng hình ảnh
                    BasketImage image = new BasketImage();
                    image.setImageUrl(url);
                    image.setBasket(basket);
                    newImages.add(image);
                } catch (IOException e) {
                    log.error("Error uploading image to Cloudinary", e);
                }
            }
            basket.getImages().addAll(newImages);
        }

        // Lưu giỏ hàng sau khi cập nhật
        basket = basketRepository.save(basket);

        // Chuyển đổi sang BasketResponse để trả về
        return basketMapper.toBasketResponse(basket);
    }
    //Get One Basket
    public BasketResponse getOneBasket(Long id){
        return basketMapper.toBasketResponse(basketRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.BASKET_NOT_EXISTED)));
    }

    //Xóa basket
    public String deleteBasket(Long id){
        Basket basket = basketRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.BASKET_NOT_EXISTED));
        basket.setStatus(0);
        basketRepository.save(basket);
        return "Giỏ hàng đã hết hàng";
    }

    //Lọc basket
    public PaginatedResponse<BasketResponse> getFilterBasket(
            int page, int size,
            String name, BigDecimal minPrice, BigDecimal maxPrice
            ,Long categoryId, int status){
        PageRequest pageRequest = PageRequest.of(page,size);
        Page<Basket> basketPage =
                basketRepository.findByNameAndPriceAndCategoryAndStatus(name, minPrice, maxPrice, categoryId, status, pageRequest);
        List<BasketResponse> basketResponses = basketPage.getContent().stream().map(basketMapper::toBasketResponse).toList();
        return PaginatedResponse.<BasketResponse>builder()
                .totalItems((int)(basketPage.getTotalElements()))
                .totalPages(basketPage.getTotalPages())
                .currentPage(basketPage.getNumber())
                .pageSize(basketPage.getSize())
                .hasNextPage(basketPage.hasNext())
                .hasPreviousPage(basketPage.hasPrevious())
                .elements(basketResponses)
                .build();
    }

    public List<BasketResponse> getAllBasket(){
        var basket = basketRepository.findAll();
        return basket.stream().map(basketMapper::toBasketResponse).toList();
    }


}
