package airbnb;

import airbnb.config.kafka.KafkaProcessor;

import java.util.Optional;

//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{

    @Autowired 
    private RentcarRepository rentCarRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPaymentApproved_ReserveCar(@Payload PaymentApproved paymentApproved){

        ////////////////////////////////////////////////////////////////////
        // 결제 완료 시 -> RentCar의 status => reserved, lastAction => reserved
        ////////////////////////////////////////////////////////////////////

        if(paymentApproved.isMe())  {
            System.out.println("\n\n##### listener ReserveCar : " + paymentApproved.toJson() + "\n\n");

            if(paymentApproved.getCarId() > 0 ) { // carId가 있는 경우. 즉 렌터카가 예약된 경우에만 진행
                // CAR 테이블 상태 업데이트
                long carId = paymentApproved.getCarId(); // 결제된 CarId
                long roomId = paymentApproved.getRoomId(); // 예약과 연관된 roomId Key
                updateCarStatus(carId, roomId,"reserved", "reserved"); // Status Update
            }
            
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPaymentCancelled_CancelCar(@Payload PaymentCancelled paymentCancelled){

        if(paymentCancelled.isMe()) {

            //////////////////////////////////////////////////////////////////////
            // 결제 취소 시 -> RentCar의 status => available, lastAction => cancelled
            //////////////////////////////////////////////////////////////////////
            System.out.println("\n\n##### listener CancelCar : " + paymentCancelled.toJson() + "\n\n");

            // CAR 테이블 상태 업데이트
            if(paymentCancelled.getCarId() > 0 ) { // carId가 있는 경우. 즉 렌터카가 예약된 경우에만 진행
                long carId = paymentCancelled.getCarId(); // 결제된 CarId
                long roomId = paymentCancelled.getRoomId(); // 예약과 연관된 roomId Key
                updateCarStatus(carId, roomId, "available", "cancelled"); // Status Update 
            }

        }

        
            
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString){}


    private void updateCarStatus(long carId, long roomId, String status, String lastAction)     {

        //////////////////////////////////////////////
        // carId의 RentCar 데이터의 status, lastAction 수정
        //////////////////////////////////////////////

        // Room 테이블에서 roomId의 Data 조회 -> room
        Optional<Rentcar> res = rentCarRepository.findById(carId);
        Rentcar rentCar = res.get();

        System.out.println("carId       : " + rentCar.getCarId());
        System.out.println("roomId      : " + rentCar.getRoomId());
        System.out.println("status      : " + rentCar.getStatus());
        System.out.println("lastAction  : " + rentCar.getLastAction());

        // room 값 수정
        rentCar.setStatus(status); // status 수정 
        rentCar.setLastAction(lastAction);  // lastAction 값 셋팅
        rentCar.setRoomId(roomId);

        System.out.println("Edited roomId     : " + rentCar.getRoomId());
        System.out.println("Edited status     : " + rentCar.getStatus());
        System.out.println("Edited lastAction : " + rentCar.getLastAction());

        /////////////
        // DB Update
        /////////////
        rentCarRepository.save(rentCar);

    }

}
