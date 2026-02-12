package com.pramaindia;

//import org.mybatis.spring.annotation.MapperScan;
import tk.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableAsync
@EnableScheduling
@MapperScan({"com.pramaindia.cust_manag.dao","com.pramaindia.role_management.dao"})

public class CustomerManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerManagementApplication.class, args);
        printStartupBanner();
    }

    private static void printStartupBanner() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🚀 Customer Management System Started Successfully");
        System.out.println("=".repeat(60));
        System.out.println("📅 Time: " + new java.util.Date());
        System.out.println("🔗 Local: http://localhost:8080");
        System.out.println("\n📚 API Documentation:");
        System.out.println("   • Swagger UI:      http://localhost:8080/swagger-ui/index.html");
        System.out.println("   • OpenAPI JSON:    http://localhost:8080/v3/api-docs");
        System.out.println("   • Health:          http://localhost:8080/actuator/health");
        System.out.println("   • Metrics:         http://localhost:8080/actuator/metrics");
        System.out.println("   • Info:            http://localhost:8080/actuator/info");
        System.out.println("\n🔐 Authentication:");
        System.out.println("   • Login Page:      http://localhost:8080/login");
        System.out.println("   • Dashboard:       http://localhost:8080/dashboard");
        System.out.println("   • Reset Password:  http://localhost:8080/reset-password");
        System.out.println("\n📋 Login Records API:");
        System.out.println("   • Test Endpoint:   http://localhost:8080/api/portal-user/ssoUser/test");
        System.out.println("   • Search Records:  POST http://localhost:8080/api/portal-user/ssoUser/pageListSsoUserLoginRecord");
        System.out.println("=".repeat(60));
        System.out.println("✅ Application is ready and waiting for requests...");
        System.out.println("=".repeat(60) + "\n");
    }
}