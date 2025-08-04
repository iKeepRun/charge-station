package com.zhuritec.service.impl;

import com.zhuritec.config.IotDBSessionConfig;
import com.zhuritec.service.IotDBService;
import jakarta.annotation.Resource;
import org.apache.iotdb.isession.pool.SessionDataSetWrapper;
import org.apache.iotdb.rpc.IoTDBConnectionException;
import org.apache.iotdb.rpc.StatementExecutionException;
import org.apache.iotdb.session.pool.SessionPool;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IotDBServiceImpl implements IotDBService {
    @Resource
    private IotDBSessionConfig iotDBSessionConfig;

    /**
     * 自定义sql语句的查询
     * @param sql
     * @return
     */
    @Override
    public SessionDataSetWrapper executeQueryStatement(String sql) {

        SessionPool pool = iotDBSessionConfig.initPool();

        SessionDataSetWrapper sessionDataSetWrapper = null;
        try {
            sessionDataSetWrapper = pool.executeQueryStatement(sql);
        } catch (IoTDBConnectionException e) {
            throw new RuntimeException(e);
        } catch (StatementExecutionException e) {
            throw new RuntimeException(e);
        }

        return sessionDataSetWrapper;
    }

    /**
     * 查询时间范围区间内（左闭右开）的数据
     * @param paths
     * @param startTime 开始时间
     * @param endTime  结束时间（不包含）
     * @param timeOut  超时时间
     * @return
     */
    @Override
    public SessionDataSetWrapper executeRawDataQuery(List<String> paths, long startTime, long endTime, long timeOut) {
        SessionPool pool = iotDBSessionConfig.initPool();
        SessionDataSetWrapper sessionDataSetWrapper = null;
        try {
            sessionDataSetWrapper = pool.executeRawDataQuery(paths, startTime, endTime, timeOut);
        } catch (IoTDBConnectionException e) {
            throw new RuntimeException(e);
        } catch (StatementExecutionException e) {
            throw new RuntimeException(e);
        }
        return sessionDataSetWrapper;
    }
}
