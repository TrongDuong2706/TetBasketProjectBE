package com.hanu.isd.service;

import com.hanu.isd.dto.request.BasketCategoryRequest;
import com.hanu.isd.dto.response.BasketCategoryResponse;
import com.hanu.isd.entity.BasketCategory;
import com.hanu.isd.exception.AppException;
import com.hanu.isd.exception.ErrorCode;
import com.hanu.isd.mapper.BasketCategoryMapper;
import com.hanu.isd.repository.BasketCategoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BasketCategoryService {
    BasketCategoryRepository basketCategoryRepository;
    BasketCategoryMapper basketCategoryMapper;

    //Create
    public BasketCategoryResponse createBasketCategory(BasketCategoryRequest request){
        BasketCategory basketCategory = basketCategoryMapper.toBasketCategory(request);
        basketCategory = basketCategoryRepository.save(basketCategory);
        return  basketCategoryMapper.toBasketCategoryResponse(basketCategory);
    }
    //Get All
    public List<BasketCategoryResponse> getAll(){
        var basketCategory = basketCategoryRepository.findAll();
        return basketCategory.stream().map(basketCategoryMapper::toBasketCategoryResponse).toList();
    }

    //Get One
    public BasketCategoryResponse getOneBasketCategory(Long id){
        return basketCategoryMapper.toBasketCategoryResponse(
                basketCategoryRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.BASKET_CATEGORY_NOT_EXISTED))
        );
    }

    // Sửa loại giỏ hàng
    public BasketCategoryResponse updateBasketCategory(Long id, BasketCategoryRequest request) {
        BasketCategory basketCategory = basketCategoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BASKET_CATEGORY_NOT_EXISTED));
        // Cập nhật thông tin từ request
        basketCategory.setName(request.getName());
        basketCategory.setDescription(request.getDescription());
        // Lưu lại thay đổi
        basketCategoryRepository.save(basketCategory);

        return basketCategoryMapper.toBasketCategoryResponse(basketCategory);
    }
    //Xóa loại giỏ hàng
    public String deleteBasketCategory(Long id) {
        if (!basketCategoryRepository.existsById(id)) {
            throw new AppException(ErrorCode.BASKET_CATEGORY_NOT_EXISTED);
        }
        basketCategoryRepository.deleteById(id);
        return "Xóa thành công";
    }
}
