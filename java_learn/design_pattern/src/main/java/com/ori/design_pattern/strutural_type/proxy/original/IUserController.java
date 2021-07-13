package com.ori.design_pattern.strutural_type.proxy.original;


public interface IUserController {
    UserVo login(String telephone, String password);

    UserVo register(String telephone, String password);
}
