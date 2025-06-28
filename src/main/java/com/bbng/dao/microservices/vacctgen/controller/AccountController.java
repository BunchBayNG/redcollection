package com.bbng.dao.microservices.vacctgen.controller;


import com.bbng.dao.microservices.vacctgen.config.MerchantSearchFilter;
import com.bbng.dao.microservices.vacctgen.config.SearchFilter;
import com.bbng.dao.microservices.vacctgen.dto.request.ActivationOperation;
import com.bbng.dao.microservices.vacctgen.dto.request.MerchantStatusRequest;
import com.bbng.dao.microservices.vacctgen.dto.request.ProvisionRequest;
import com.bbng.dao.microservices.vacctgen.dto.request.StatusRequest;
import com.bbng.dao.microservices.vacctgen.dto.response.ActivationResponse;
import com.bbng.dao.microservices.vacctgen.dto.response.ConfirmLookupResult;
import com.bbng.dao.microservices.vacctgen.dto.response.LookUpResult;
import com.bbng.dao.microservices.vacctgen.dto.response.ProvisionResult;
import com.bbng.dao.microservices.vacctgen.entity.ProvisionedAccount;
import com.bbng.dao.microservices.vacctgen.impl.AccountManager;
import com.bbng.dao.microservices.vacctgen.value.GenerateValue;
import com.bbng.dao.microservices.vacctgen.value.MerchantProvisionValue;
import com.bbng.dao.util.exceptions.customExceptions.ResourceNotFoundException;
import com.bbng.dao.util.response.ResponseDto;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@RestController
@RequestMapping("${apiVersion}" + "/virtual-account/")
public class AccountController {
    private static final Logger log = LoggerFactory.getLogger(AccountController.class);
    private final AccountManager accountManager;

    public AccountController(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    @PostMapping("merchant/provision")
    public ResponseEntity<ResponseDto<ProvisionedAccount>> provisionForMerchant(
            @RequestBody MerchantProvisionValue request
    ) {

        return  ResponseEntity.status(HttpStatus.OK).body(accountManager.provisionForMerchant(request));
    }

    @PostMapping("merchant/provision-static")
    public ResponseEntity<ResponseDto<ProvisionedAccount>> provisionStaticForMerchant(
            @RequestBody MerchantProvisionValue request
    ) {

        return  ResponseEntity.status(HttpStatus.OK).body( accountManager.provisionStaticForMerchant(request));
    }

    @GetMapping("merchant/status")
    public ResponseEntity<ResponseDto<ProvisionedAccount>> getMerchantStatus(
            MerchantStatusRequest statusRequest) {

        return  ResponseEntity.status(HttpStatus.OK).body(accountManager.getProvisionedAccountStatus(statusRequest));
    }

//
//    @GetMapping("search")
//    public ResponseEntity<ResponseDto<List<ProvisionedAccount>>> search(SearchFilter filter, BindingResult bindingResult) {
//        if (bindingResult.hasGlobalErrors()) {
//            log.debug("has global error(s): {}", bindingResult.getGlobalErrors().stream()
//                    .map(ObjectError::toString).collect(Collectors.toList()));
//            /// return Response.failed(bindingResult.getFieldError().getDefaultMessage());
//            throw new ResourceNotFoundException(Objects.requireNonNull(bindingResult.getGlobalError()).getDefaultMessage());
//        }
//
//        if (bindingResult.hasFieldErrors()) {
//            log.debug("Field error(s): {}", bindingResult.getFieldErrors().stream()
//                    .map(FieldError::toString).collect(Collectors.toList()));
//          ///  return Response.failed(bindingResult.getFieldError().getDefaultMessage());
//            throw new ResourceNotFoundException(Objects.requireNonNull(bindingResult.getGlobalError()).getDefaultMessage());
//        }
//
//        return  ResponseEntity.status(HttpStatus.OK).body( accountManager.search(filter));
//    }
//
//
//
//    @GetMapping("merchant/search")
//    public ResponseEntity<ResponseDto<List<ProvisionedAccount>>> merchantSearch(
//            MerchantSearchFilter filter, BindingResult bindingResult ) {
//        if (bindingResult.hasGlobalErrors()) {
//            log.debug("has global error(s): {}", bindingResult.getGlobalErrors().stream()
//                    .map(ObjectError::toString).collect(Collectors.toList()));
//            /// return Response.failed(bindingResult.getFieldError().getDefaultMessage());
//            throw new ResourceNotFoundException(Objects.requireNonNull(bindingResult.getGlobalError()).getDefaultMessage());
//        }
//
//        if (bindingResult.hasFieldErrors()) {
//            log.debug("Field error(s): {}", bindingResult.getFieldErrors().stream()
//                    .map(FieldError::toString).collect(Collectors.toList()));
//            /// return Response.failed(bindingResult.getFieldError().getDefaultMessage());
//            throw new ResourceNotFoundException(Objects.requireNonNull(bindingResult.getGlobalError()).getDefaultMessage());
//        }
//
//        return  ResponseEntity.status(HttpStatus.OK).body(accountManager.search(filter));
//    }
//

    @GetMapping("lookup")
    public ResponseEntity<ResponseDto<LookUpResult>> lookup(@RequestParam("accountNo") String accountNo) {

        return  ResponseEntity.status(HttpStatus.OK).body( accountManager.doLookup(accountNo));
    }

    @GetMapping("confirmLookup")
    public ResponseEntity<ResponseDto<ConfirmLookupResult>> confirmLookup(@RequestParam("accountNo") String accountNo, @RequestParam("amount") String amount) {

        return  ResponseEntity.status(HttpStatus.OK).body( accountManager.doConfirmLookup(accountNo, amount));
    }

    @GetMapping("confirmLookupAllStatus")
    public ResponseEntity<ResponseDto<ConfirmLookupResult>> doConfirmLookupAllStatus(@RequestParam("accountNo") String accountNo, @RequestParam("amount") String amount) {

        return  ResponseEntity.status(HttpStatus.OK).body(accountManager.doConfirmLookupAllStatus(accountNo, amount));
    }

    @PostMapping("activate")
    public ResponseEntity<ResponseDto<ActivationResponse>> activate(@RequestBody ActivationOperation request) {
        int activated = accountManager.activationOperation(request,
                ActivationOperation.Operation.ACTIVATE);
        final ActivationResponse response = ActivationResponse
                .builder().numberActivated(activated).build();

        return  ResponseEntity.status(HttpStatus.OK).body(ResponseDto.<ActivationResponse>builder()
                .statusCode(200)
                .status(true)
                .message("Activated %d account(s) successfully")
                .data(response)
                .build());

    }

    @PostMapping("deactivate")
    public ResponseEntity<ResponseDto<ActivationResponse>> deactivate(@RequestBody ActivationOperation request) {
        int activated = accountManager.activationOperation(request,
                ActivationOperation.Operation.DEACTIVATE);
        final ActivationResponse response = ActivationResponse
                .builder().numberDeactivated(activated).build();

        return  ResponseEntity.status(HttpStatus.OK).body(ResponseDto.<ActivationResponse>builder()
                .statusCode(200)
                .status(true)
                .message("Deactivated %d account(s) successfully")
                .data(response)
                .build());



    }

/*    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleValidationError(MethodArgumentNotValidException ex) {
        final String errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(". "));
        return ResponseEntity.notFound(errors);
        return ResponseEntity.ok(Response.failed(errors));
       // return  ResponseEntity.status(HttpStatus.OK).body(customerService.getOrgCustomers(request));
    }

 */
}
