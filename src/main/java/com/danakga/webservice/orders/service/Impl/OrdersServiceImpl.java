package com.danakga.webservice.orders.service.Impl;

import com.danakga.webservice.company.model.CompanyInfo;
import com.danakga.webservice.company.repository.CompanyRepository;
import com.danakga.webservice.exception.CustomException;
import com.danakga.webservice.orders.dto.request.OrdersDto;
import com.danakga.webservice.orders.dto.request.StatusDto;
import com.danakga.webservice.orders.dto.response.ResOrdersDto;
import com.danakga.webservice.orders.dto.response.ResOrdersListDto;
import com.danakga.webservice.orders.dto.response.ResSalesDto;
import com.danakga.webservice.orders.dto.response.ResSalesListDto;
import com.danakga.webservice.orders.model.OrdersStatus;
import com.danakga.webservice.orders.model.Orders;
import com.danakga.webservice.orders.repository.OrdersRepository;
import com.danakga.webservice.orders.service.OrdersService;
import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.product.repository.ProductRepository;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrdersServiceImpl implements OrdersService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrdersRepository ordersRepository;
    private final CompanyRepository companyRepository;

    //상품 주문 등록
    @Override
    public Long ordersSave(UserInfo userInfo, Long productId, OrdersDto ordersDto) {

        UserInfo ordersUserInfo = userRepository.findById(userInfo.getId()).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("로그인 사용자를 찾을 수 없습니다")
        );
        Product ordersProduct = productRepository.findByProductId(productId).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("해당 상품을 찾을 수 없습니다")
        );

        Orders orders = ordersRepository.save(
                Orders.builder()
                        .userInfo(ordersUserInfo)
                        .product(ordersProduct)
                        .ordersStatus(OrdersStatus.READY.getStatus())
                        .ordersDate(LocalDateTime.now())
                        .ordersFinishedDate(null) //배송완료시에 입력됨
                        .ordersPrice(ordersDto.getOrdersPrice())
                        .ordersQuantity(ordersDto.getOrdersQuantity())
                        .ordersTrackingNum(null) //배송완료시에 입력됨
                        .build()
        );

        //제품 수량 변경
        if(ordersProduct.getProductStock() < ordersDto.getOrdersQuantity()){
            return -1L; // 재고 부족
        }else{
            productRepository.updateProductStock(ordersDto.getOrdersQuantity(),ordersProduct.getProductId());
        }

        return orders.getOrdersId();
    }

    //주문 내역
    @Override
    public List<ResOrdersListDto> ordersList(UserInfo userInfo, Pageable pageable, int page, LocalDateTime startDate,LocalDateTime endDate) {
        UserInfo ordersUserInfo = userRepository.findById(userInfo.getId()).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("로그인 사용자를 찾을 수 없습니다")
        );
        pageable = PageRequest.of(page, 10, Sort.by("ordersId").descending());

        Page<Orders> ordersPage = ordersRepository.ordersList(ordersUserInfo,pageable,startDate,endDate);
        List<Orders> ordersList = ordersPage.getContent();

        List<ResOrdersListDto> ordersListDto = new ArrayList<>();

        ordersList.forEach(entity->{
                ResOrdersListDto listDto = new ResOrdersListDto();
                listDto.setOrdersId(entity.getOrdersId());
                listDto.setOrdersDate(entity.getOrdersDate());
                listDto.setCompanyName(entity.getProduct().getProductCompanyId().getCompanyName());
                listDto.setProductId(entity.getProduct().getProductId());
                listDto.setProductBrand(entity.getProduct().getProductBrand());
                listDto.setProductName(entity.getProduct().getProductName());
                listDto.setOrdersQuantity(entity.getOrdersQuantity());
                listDto.setOrdersPrice(entity.getOrdersPrice());
                listDto.setOrderStatus(entity.getOrdersStatus());
                listDto.setTotalPage(ordersPage.getTotalPages());
                listDto.setTotalElement(ordersPage.getTotalElements());
                ordersListDto.add(listDto);
                }
        );
        return ordersListDto;
    }

    //주문 상세 내역
    @Override
    public ResOrdersDto ordersDetail(UserInfo userInfo, Long ordersId) {
        UserInfo ordersUserInfo = userRepository.findById(userInfo.getId()).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("로그인 사용자를 찾을 수 없습니다.")
        );
        Orders orders = ordersRepository.findByOrdersIdAndUserInfo(ordersId,ordersUserInfo).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("주문 정보를 찾을 수 없습니다.")
        );

        return ResOrdersDto.builder()
                .ordersId(orders.getOrdersId())
                .companyName(orders.getProduct().getProductCompanyId().getCompanyName())
                .productBrand(orders.getProduct().getProductBrand())
                .productName(orders.getProduct().getProductName())
                .ordersQuantity(orders.getOrdersQuantity())
                .ordersPrice(orders.getOrdersPrice())
                .ordersDate(orders.getOrdersDate())
                .ordersStatus(orders.getOrdersStatus())
                .ordersFinishedDate(orders.getOrdersFinishedDate())
                .ordersTrackingNum(orders.getOrdersTrackingNum())
                .userName(orders.getUserInfo().getName())
                .userPhone(orders.getUserInfo().getPhone())
                .userAdrNum(orders.getUserInfo().getUserAdrNum())
                .userStreetAdr(orders.getUserInfo().getUserStreetAdr())
                .userLotAdr(orders.getUserInfo().getUserLotAdr())
                .userDetailAdr(orders.getUserInfo().getUserDetailAdr())
                .build();
    }


    //판매내역
    @Override
    public List<ResSalesListDto> salesList(UserInfo userInfo, Pageable pageable, int page, LocalDateTime startDate, LocalDateTime endDate) {
        UserInfo salesUserInfo = userRepository.findById(userInfo.getId()).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("로그인 사용자를 찾을 수 없습니다.")
        );
        CompanyInfo salesCompanyInfo = companyRepository.findByUserInfo(salesUserInfo).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("사용자의 회사정보를 찾을 수 없습니다.")
        );
        pageable = PageRequest.of(page, 10, Sort.by("ordersId").descending());
        Page<Orders> salesPage = ordersRepository.salesList(salesCompanyInfo.getCompanyId(),pageable,startDate,endDate);
        List<Orders> salesList = salesPage.getContent();

        List<ResSalesListDto> salesListDto = new ArrayList<>();
        salesList.forEach(entity->{
            ResSalesListDto listDto = new ResSalesListDto();
                    listDto.setOrdersId(entity.getOrdersId());
                    listDto.setProductId(entity.getProduct().getProductId());
                    listDto.setProductBrand(entity.getProduct().getProductBrand());
                    listDto.setProductName(entity.getProduct().getProductName());
                    listDto.setOrdersQuantity(entity.getOrdersQuantity());
                    listDto.setOrdersPrice(entity.getOrdersPrice());
                    listDto.setOrdersDate(entity.getOrdersDate());
                    listDto.setOrderStatus(entity.getOrdersStatus());
                    listDto.setTotalPage(salesPage.getTotalPages());
                    listDto.setTotalElement(salesPage.getTotalElements());
                    salesListDto.add(listDto);
                }
        );
        return salesListDto;

    }

    //사업자 - 판매 내역 상세 정보
    @Override
    public ResSalesDto salesDetail(UserInfo userInfo,Long ordersId) {
        UserInfo salesUserInfo = userRepository.findById(userInfo.getId()).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("로그인 사용자를 찾을 수 없습니다.")
        );
        CompanyInfo salesCompanyInfo = companyRepository.findByUserInfo(salesUserInfo).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("사용자의 회사정보를 찾을 수 없습니다.")
        );
        Orders orders = ordersRepository.findByOrdersIdAndCompany(ordersId,salesCompanyInfo.getCompanyId()).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("주문 정보를 찾을 수 없습니다.")
        );
        return ResSalesDto.builder()
                .ordersId(orders.getOrdersId())
                //유저정보
                .userId(orders.getUserInfo().getUserid())
                .userName(orders.getUserInfo().getName())
                .userPhone(orders.getUserInfo().getPhone())
                .userAdrNum(orders.getUserInfo().getUserAdrNum())
                .userStreetAdr(orders.getUserInfo().getUserStreetAdr())
                .userLotAdr(orders.getUserInfo().getUserLotAdr())
                .userDetailAdr(orders.getUserInfo().getUserStreetAdr())
                //상품정보
                .productBrand(orders.getProduct().getProductBrand())
                .productName(orders.getProduct().getProductName())
                .ordersQuantity(orders.getOrdersQuantity())
                .ordersPrice(orders.getOrdersPrice())
                .ordersDate(orders.getOrdersDate())
                .orderStatus(orders.getOrdersStatus())
                .ordersFinishedDate(orders.getOrdersFinishedDate())
                .ordersTrackingNum(orders.getOrdersTrackingNum())
                .build();
    }

    //사업자 - 판매 내역 상태 업데이트
    @Transactional
    @Override
    public Long updateSalesStatus(UserInfo userInfo, Long ordersId, StatusDto statusDto) {
        UserInfo updateSalesUserInfo = userRepository.findById(userInfo.getId()).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("로그인 사용자를 찾을 수 없습니다.")
        );
        CompanyInfo updateSalesCompanyInfo = companyRepository.findByUserInfo(updateSalesUserInfo).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("사용자의 회사정보를 찾을 수 없습니다.")
        );
        Orders orders = ordersRepository.findByOrdersId(ordersId).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("판매내역을 찾을 수 없습니다.")
        );
        String inputStatus = orders.getOrdersStatus();
        String trackingNum = statusDto.getOrdersTrackingNum();
        String status = null;

        if (inputStatus.equals(OrdersStatus.READY.getStatus()) && trackingNum != null) {
            status = OrdersStatus.START.getStatus(); //배송시작은 운송장 번호 입력하기
            ordersRepository.updateSalesTrackingNum(trackingNum,updateSalesCompanyInfo.getCompanyId(),ordersId);
        }else if (inputStatus.equals(OrdersStatus.CANCEL.getStatus()) || inputStatus.equals(OrdersStatus.RETURN.getStatus())) {
            status = OrdersStatus.REFUND.getStatus(); //환불
        }else if (inputStatus.equals(OrdersStatus.EXCHANGE.getStatus())) {
            status = OrdersStatus.REDELIVERY.getStatus(); //반품배송
            ordersRepository.updateSalesTrackingNum(trackingNum,updateSalesCompanyInfo.getCompanyId(),ordersId);
        }else if (inputStatus.equals(OrdersStatus.START.getStatus())) {
            status = OrdersStatus.FINISH.getStatus(); //배송완료 처리할때는 주문완료 날짜 입력하기
            ordersRepository.updateSalesFinishedDate(LocalDateTime.now(),updateSalesCompanyInfo.getCompanyId(),ordersId);
        }else if(inputStatus.equals(OrdersStatus.REDELIVERY.getStatus())){
            status = OrdersStatus.FINISH.getStatus();
        }else{
            return -1L;
        }
        ordersRepository.updateSalesStatus(status,updateSalesCompanyInfo.getCompanyId(),orders.getOrdersId());

        return ordersId;
    }

    //일반사용자(구매자) - 주문내역 업데이트
    @Transactional
    @Override
    public Long updateOrdersStatus(UserInfo userInfo, Long ordersId, StatusDto statusDto) {
        UserInfo updateSalesUserInfo = userRepository.findById(userInfo.getId()).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("로그인 사용자를 찾을 수 없습니다.")
        );
        Orders orders = ordersRepository.findByOrdersId(ordersId).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("판매내역을 찾을 수 없습니다.")
        );
        String inputStatus = orders.getOrdersStatus();
        String changeStatus = statusDto.getChangeOrdersStatus();
        String status = null;

        if(inputStatus.equals(OrdersStatus.READY.getStatus())){
            status = OrdersStatus.CANCEL.getStatus();
        }
        else if (inputStatus.equals(OrdersStatus.FINISH.getStatus())&& changeStatus.equals(OrdersStatus.CONFIRM.getStatus())) {
            status = OrdersStatus.CONFIRM.getStatus(); //구매확정
        }
        else if (inputStatus.equals(OrdersStatus.FINISH.getStatus())&& changeStatus.equals(OrdersStatus.RETURN.getStatus())) {
            status = OrdersStatus.RETURN.getStatus(); //반품신청
        }
        else if (inputStatus.equals(OrdersStatus.FINISH.getStatus())&& changeStatus.equals(OrdersStatus.EXCHANGE.getStatus())) {
            status = OrdersStatus.EXCHANGE.getStatus(); //교환신청
        }else {
            return -1L;
        }
        ordersRepository.updateOrdersStatus(updateSalesUserInfo,status,orders.getOrdersId());

        return ordersId;
    }

}
