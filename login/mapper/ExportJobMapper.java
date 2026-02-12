package com.pramaindia.login.mapper;

import com.pramaindia.login.model.entity.ExportJobEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface ExportJobMapper {

    // Insert methods
    int insertExportJob(ExportJobEntity entity);

    // Select methods
    ExportJobEntity selectExportJobById(@Param("id") String id);
    List<ExportJobEntity> selectExportJobsByEmail(@Param("userEmail") String userEmail,
                                                  @Param("limit") Integer limit);
    List<ExportJobEntity> selectPendingJobs(@Param("limit") int limit);
    List<ExportJobEntity> selectExpiredJobs(@Param("limit") int limit);
    List<ExportJobEntity> selectStaleProcessingJobs(@Param("limit") int limit);

    // Search methods
    List<ExportJobEntity> searchExportJobs(@Param("userEmail") String userEmail,
                                           @Param("status") String status,
                                           @Param("startDate") Date startDate,
                                           @Param("endDate") Date endDate,
                                           @Param("limit") Integer limit);

    List<ExportJobEntity> searchExportJobsWithPagination(@Param("userEmail") String userEmail,
                                                         @Param("status") String status,
                                                         @Param("startDate") Date startDate,
                                                         @Param("endDate") Date endDate,
                                                         @Param("limit") Integer limit,
                                                         @Param("offset") Integer offset);

    List<ExportJobEntity> searchExportJobsAdmin(@Param("userEmail") String userEmail,
                                                @Param("status") String status,
                                                @Param("startDate") Date startDate,
                                                @Param("endDate") Date endDate,
                                                @Param("limit") Integer limit);

    // Update methods
    int updateExportJobStatus(@Param("id") String id,
                              @Param("status") String status,
                              @Param("errorMessage") String errorMessage,
                              @Param("filePath") String filePath,
                              @Param("fileName") String fileName);

    int updateExportJobFilePath(@Param("id") String id,
                                @Param("filePath") String filePath,
                                @Param("fileName") String fileName);

    int updateExportJobError(@Param("id") String id,
                             @Param("errorMessage") String errorMessage);

    int incrementDownloadCount(@Param("id") String id);

    int cancelExportJob(@Param("id") String id);

    int markJobAsProcessing(@Param("id") String id);

    int extendExpiryTime(@Param("id") String id);

    // Delete methods
    int deleteOldJobs();

    // Statistics methods
    List<Map<String, Object>> countJobsByStatus();
    List<Map<String, Object>> getExportStats();
    List<Map<String, Object>> getTopUsersByExports();
    List<ExportJobEntity> getFailedJobsWithRetryCount(@Param("limit") int limit);
}