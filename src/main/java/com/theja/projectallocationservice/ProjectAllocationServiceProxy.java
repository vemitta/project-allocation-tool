package com.theja.projectallocationservice;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "project-allocation-service")
public interface ProjectAllocationServiceProxy {
}
