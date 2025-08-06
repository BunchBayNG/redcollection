package com.bbng.dao.microservices.auth.config.impl;


import com.bbng.dao.microservices.auth.config.dto.request.ConfigSetupDto;
import com.bbng.dao.microservices.auth.config.entity.SystemConfigEntity;
import com.bbng.dao.microservices.auth.config.repository.SystemConfigRepository;
import com.bbng.dao.microservices.auth.config.service.ConfigService;
import com.bbng.dao.microservices.vacctnumnotify.entity.BankAccountEntity;
import com.bbng.dao.microservices.vacctnumnotify.entity.BankCategory;
import com.bbng.dao.microservices.vacctnumnotify.repository.BankAccountRepository;
import com.bbng.dao.util.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ConfigServiceImpl implements ConfigService {

    private final SystemConfigRepository systemConfigRepository;
    private final BankAccountRepository bankAccountRepository;


    @Override
    public ResponseDto<String> updateUserConfiguration(ConfigSetupDto request) {


        BankAccountEntity adminAccount = bankAccountRepository.findFirstByBankCategory(BankCategory.ADMIN)
                .orElse(createBankAccount(request.getAdminAccountNo(), BankCategory.ADMIN ));


        BankAccountEntity platformAccount = bankAccountRepository.findFirstByBankCategory(BankCategory.PLATFORM)
                .orElse(createBankAccount(request.getAdminAccountNo(), BankCategory.PLATFORM ));


        BankAccountEntity vatAccount = bankAccountRepository.findFirstByBankCategory(BankCategory.ISO)
                .orElse(createBankAccount(request.getAdminAccountNo(), BankCategory.ISO ));


        SystemConfigEntity config = getSystemConfigEntity(request,
                adminAccount.getBankAccountNumber(), platformAccount.getBankAccountNumber(),vatAccount.getBankAccountNumber() );


        systemConfigRepository.save(config);

        return ResponseDto.<String>builder()
                .statusCode(200)
                .status(true)
                .message("User configuration updated successfully")
                .data(String.format("User with id: %s has been updated.", request.getUserId()))
                .build();
    }


    public ResponseDto<SystemConfigEntity>  getConfigResponse() {

        return ResponseDto.<SystemConfigEntity>builder()
                .statusCode(200)
                .status(true)
                .message("User configuration updated successfully")
                .data(systemConfigRepository.findById(1L).orElseThrow())
                .build();
    }


    public SystemConfigEntity  getConfig() {

        return systemConfigRepository.findById(1L).orElseThrow();
    }




    private static SystemConfigEntity getSystemConfigEntity(ConfigSetupDto req, String adminAcctNo, String platformAcctNo, String vatAcctNo) {

        SystemConfigEntity config = new SystemConfigEntity();
        config.setId(1L);
        config.setCommissionPercent(req.getCommissionPercent());
        config.setCommissionCap(req.getCommissionCap());
        config.setAdminSplitPercent(req.getAdminSplitPercent());
        config.setPlatformSplitPercent(req.getPlatformSplitPercent());
        config.setVatPercent(req.getVatPercent());
        config.setPlatformAccountNo(req.getPlatformAccountNo());
        config.setAdminAccountNo(adminAcctNo);
        config.setPlatformAccountNo(platformAcctNo);
        config.setVatAccountNo(vatAcctNo);
        return config;
    }


    private BankAccountEntity createBankAccount(String bankAccountNumber, BankCategory bankCategory) {

        BankAccountEntity acct = new BankAccountEntity();
        acct.setBankAccountNumber(bankAccountNumber);
        acct.setBankCategory(bankCategory);
        acct.setBankAccountName(  "UBA- " +bankCategory.name() + "-ACCT");
        bankAccountRepository.save(acct);
        return acct;



    }
    
}
