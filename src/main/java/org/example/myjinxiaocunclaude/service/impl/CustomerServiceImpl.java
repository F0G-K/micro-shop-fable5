package org.example.myjinxiaocunclaude.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.myjinxiaocunclaude.entity.Customer;
import org.example.myjinxiaocunclaude.mapper.CustomerMapper;
import org.example.myjinxiaocunclaude.service.CustomerService;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {
}
