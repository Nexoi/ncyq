package com.seeu.ywq.api.release.user;

import com.seeu.core.R;
import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.user.model.Address;
import com.seeu.ywq.user.service.AddressService;
import com.seeu.ywq.userlogin.model.UserLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by suneo.
 * User: neo
 * Date: 24/01/2018
 * Time: 4:32 PM
 * Describe:
 */

@Api(tags = "用户个人中心收货地址信息", description = "收货地址")
@RestController
@RequestMapping("/api/v1/user/address")
public class AddressApi {
    @Autowired
    private AddressService addressService;

    @ApiOperation("列出地址")
    @GetMapping("/list")
    @PreAuthorize("hasRole('USER')")
    public List listMine(@AuthenticationPrincipal UserLogin authUser) {
        return addressService.findOneByUid(authUser.getUid());
    }

    @ApiOperation("查看某一条地址")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity get(@AuthenticationPrincipal UserLogin authUser,
                              @PathVariable Long id) {
        try {
            return ResponseEntity.ok(addressService.findOneByUid(id, authUser.getUid()));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(R.code(404).message("找不到该地址"));
        }
    }

    @ApiOperation("添加新地址")
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public Address add(@AuthenticationPrincipal UserLogin authUser,
                       Address address) {
        address.setUid(authUser.getUid());
        return addressService.save(address);
    }

    @ApiOperation("修改地址")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity update(@AuthenticationPrincipal UserLogin authUser,
                                 @PathVariable Long id,
                                 Address address) {
        address.setId(id);
        address.setUid(authUser.getUid());
        try {
            return ResponseEntity.ok(addressService.update(address));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(R.code(404).message("找不到该地址"));
        }
    }

}
