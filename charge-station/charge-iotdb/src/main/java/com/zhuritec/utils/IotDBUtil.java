package com.zhuritec.utils;

import com.zhuritec.service.IotDBService;
import jakarta.annotation.Resource;
import org.apache.iotdb.isession.pool.SessionDataSetWrapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IotDBUtil {

    @Resource
    private IotDBService iotDBService;

    public SessionDataSetWrapper executeQueryStatement(String sql) {
        return iotDBService.executeQueryStatement(sql);
    }
    public SessionDataSetWrapper executeRawDataQuery(
            List<String> paths, long startTime, long endTime, long timeOut) {
        return iotDBService.executeRawDataQuery(paths, startTime, endTime, timeOut);
            }

}
