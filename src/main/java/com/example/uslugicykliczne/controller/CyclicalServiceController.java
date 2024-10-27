package com.example.uslugicykliczne.controller;

import com.example.uslugicykliczne.ValidationUtility;
import com.example.uslugicykliczne.dataTypes.CyclicalServiceDto;
import com.example.uslugicykliczne.dataTypes.projections.CertificateProjectionRecord;
import com.example.uslugicykliczne.dataTypes.projections.CyclicalServiceProjection;
import com.example.uslugicykliczne.dataTypes.ServiceRenewalRecord;
import com.example.uslugicykliczne.dataTypes.StatusEnum;
import com.example.uslugicykliczne.entity.CyclicalServiceEntity;
import com.example.uslugicykliczne.repo.CyclicalServiceRepo;
import com.example.uslugicykliczne.dataTypes.projections.StatusChangeRecordProjection;
import com.example.uslugicykliczne.repo.SERVICE_FINDING_MODE;
import com.example.uslugicykliczne.repo.StatusChangeRepo;
import com.example.uslugicykliczne.services.CyclicalServiceService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cyclicalservice")
@RequiredArgsConstructor
public class CyclicalServiceController {



    public record Comment(Optional<@Size(max = 255, message = "Specified comment is too long")String> comment){};
    public record StatusAndComment(Optional<@Size(max = 255, message = "Specified comment is too long")String> comment,
                                   @NotNull(message = "Nie określono statusu")
                                   @DecimalMin(value = "0", message = "Status ma być nieujemną liczbą")
                                   Integer requestedStateChange){};

    private final CyclicalServiceRepo cyclicalServiceRepo;
    private final ValidationUtility validationUtility;
    private final CyclicalServiceService cyclicalServiceService;
    private final StatusChangeRepo statusChangeRepo;

    @GetMapping("/getAllByUser")
    public List<CyclicalServiceProjection> getAllByUser(@RequestParam() Integer userID) {
        return cyclicalServiceService.getAllByFindingMode(userID,SERVICE_FINDING_MODE.BY_USER_ID);
    }

    @GetMapping("/getAllByBusiness")
    public List<CyclicalServiceProjection> getAllByBusiness(@RequestParam() Integer businessID) {
        return cyclicalServiceService.getAllByFindingMode(businessID,SERVICE_FINDING_MODE.BY_BUSINESS_ID);
    }

    @GetMapping("/getAll")
    public List<CyclicalServiceProjection> getAllServices(@RequestParam(required = false) String days, HttpServletResponse httpServletResponse){
        if(days == null)
            return cyclicalServiceService.getAllByFindingMode(-1, SERVICE_FINDING_MODE.IN_NEXT_N_DAYS);

        int nDays = 7;
        if (days.equals("7") || days.equals("14")||days.equals("30")||days.equals("60"))
            nDays = Integer.parseInt(days);
        return cyclicalServiceService.getAllByFindingMode(nDays, SERVICE_FINDING_MODE.IN_NEXT_N_DAYS);
    }


    @GetMapping("/statusChangeHistory/{id}")
    public List<StatusChangeRecordProjection> statusChangeRecordProjections(@PathVariable Integer id){
        return cyclicalServiceService.getStatusChangesRelatedToService(id);
    }
    @GetMapping("/certificateHistory/{id}")
    public List<CertificateProjectionRecord> certificateRecordProjections(@PathVariable Integer id){
        return cyclicalServiceService.getCertificatesRelatedToService(id);
    }
    @PostMapping("/statusChange/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable Integer id, @Validated @RequestBody StatusAndComment statusAndComment, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(validationUtility.validationMessagesToJSON(bindingResult));
        }

        if (statusAndComment.requestedStateChange.equals(StatusEnum.RENEWED.getMaskValue())||
                statusAndComment.requestedStateChange.equals(StatusEnum.MARKED_FOR_CANCEL.getMaskValue()))
            return ResponseEntity.badRequest().body("Ej ej ej, od tego jest osobny endpoint :>");

        boolean statusIsCorrect = false;
        for(var curEnum : StatusEnum.values()){
            if(statusAndComment.requestedStateChange.equals(curEnum.getMaskValue())){
                statusIsCorrect = true;
                break;
            }
        }
        if (!statusIsCorrect)
            return ResponseEntity.badRequest().body("Określono nie poprawny status");

        return cyclicalServiceService.changeServiceStatusAndUpdateDB(id,statusAndComment.requestedStateChange,statusAndComment.comment.orElseGet(() -> null));
    }

    @PostMapping("/renewalRequest/{id}")
    public ResponseEntity<?> requestRenewal(@PathVariable Integer id, @Validated @RequestBody Comment comment, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(validationUtility.validationMessagesToJSON(bindingResult));
        }
        return cyclicalServiceService.requestRenewal(id,comment.comment.orElseGet(() -> null));
    }

    @PostMapping("/cancelRequest/{id}")
    public ResponseEntity<?> cancelRequest(@PathVariable Integer id, @Validated @RequestBody Comment comment, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(validationUtility.validationMessagesToJSON(bindingResult));
        }
        return cyclicalServiceService.cancelRequest(id,comment.comment.orElseGet(() -> null));
    }

    @PostMapping("/renew/{id}")
    public ResponseEntity<String> renew (@Validated @RequestBody ServiceRenewalRecord serviceRenewalRecord, BindingResult bindingResult, @PathVariable Integer id ){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(validationUtility.validationMessagesToJSON(bindingResult));
        }
        return cyclicalServiceService.renewCyclicalService(serviceRenewalRecord,id);
    }

    @GetMapping("/getAllCancelRequests")
    public List<CyclicalServiceProjection> getAllCancelRequests(){
        return cyclicalServiceRepo.customFindCyclicalProjectionsWithCancelRequest();
    }


    @PostMapping("/insertBody")
    public ResponseEntity<String> insert(@Validated @RequestBody CyclicalServiceDto cyclicalServiceDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(validationUtility.validationMessagesToJSON(bindingResult));
        }
        return cyclicalServiceService.insertNewCyclicalServiceEntity(cyclicalServiceDto);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Integer id, @Valid @RequestBody() CyclicalServiceDto cyclicalServiceDto, BindingResult bindingResult ){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(validationUtility.validationMessagesToJSON(bindingResult));
        }
        return cyclicalServiceService.updateCyclicalServiceEntity(id, cyclicalServiceDto);
    }

    ///TODO masowe usuwanie certyfikatów
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete (@PathVariable Integer id ){
        statusChangeRepo.deleteByCyclicalService(id);
        cyclicalServiceRepo.deleteById(id);
        return ResponseEntity.ok().body("Cyclical service was deleted");

    }


//    @GetMapping("/get/{id}")
//    public ResponseEntity<CyclicalServiceEntity> getCyclicalService (@PathVariable Integer id ){
//        Optional<CyclicalServiceEntity> soughtEntity = cyclicalServiceRepo.findById(id);
//        if(soughtEntity.isPresent()){
//            return ResponseEntity.ok(soughtEntity.get());
//        }
//        else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//

}
