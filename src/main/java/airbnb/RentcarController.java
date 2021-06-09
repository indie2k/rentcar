package airbnb;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import java.util.List;

 @RestController
 public class RentcarController {


        @Autowired
        RentcarRepository rentcarRepository;

        @RequestMapping(value = "/chkcar/chkStatus",
                        method = RequestMethod.GET,
                        produces = "application/json;charset=UTF-8")

        public boolean chkStatus(HttpServletRequest request, HttpServletResponse response) throws Exception {

                System.out.println("######################################################");
                System.out.println("##### /rentCar/chkcar/chkStatuschkStatus  called #####");
                System.out.println("######################################################");

                // Parameter로 받은 RoomID 추출
                long carId = Long.valueOf(request.getParameter("carId"));
                System.out.println("######################## chkStatus carId : " + carId);

                // carId 데이터 조회
                Optional<Rentcar> res = rentcarRepository.findById(carId);
                Rentcar rentCar = res.get(); // 조회한 ROOM 데이터
                System.out.println("######################## chkStatus rentCar.getStatus() : " + rentCar.getStatus());

                // rentcar의 상태가 available이면 true
                boolean result = false;
                if(rentCar.getStatus().equals("available")) {
                        result = true;
                } 

                System.out.println("######################## chkStatus Return : " + result);
                
                return result;
        }
 }
