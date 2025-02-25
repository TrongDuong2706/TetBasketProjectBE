package com.hanu.isd.service;

import com.hanu.isd.dto.request.BasketCategoryRequest;
import com.hanu.isd.dto.request.BasketShellRequest;
import com.hanu.isd.dto.response.BasketCategoryResponse;
import com.hanu.isd.dto.response.BasketShellResponse;
import com.hanu.isd.entity.BasketCategory;
import com.hanu.isd.entity.BasketShell;
import com.hanu.isd.exception.AppException;
import com.hanu.isd.exception.ErrorCode;
import com.hanu.isd.mapper.BasketShellMapper;
import com.hanu.isd.repository.BasketShellRepository;
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
public class BasketShellService {
    BasketShellMapper basketShellMapper;
    BasketShellRepository basketShellRepository;

    public BasketShellResponse createBasketShell(BasketShellRequest request){
        BasketShell basketShell = basketShellMapper.toBasketShell(request);
        basketShell = basketShellRepository.save(basketShell);
        return  basketShellMapper.toBasketShellResponse(basketShell);
    }
    //Get All
    public List<BasketShellResponse> getAllBasketShell(){
        var basketShell = basketShellRepository.findAll();
        return basketShell.stream().map(basketShellMapper::toBasketShellResponse).toList();
    }

    //Get One
    public BasketShellResponse getOneBasketShell(Long id){
        return basketShellMapper.toBasketShellResponse(
                basketShellRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.BASKET_SHELL_NOT_EXISTED))
        );
    }
}
