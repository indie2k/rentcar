package airbnb;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
//import java.util.List;
//import java.util.Date;

@Entity
@Table(name="Rentcar_table")
public class Rentcar {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long carId;
    private String carName;
    private String status;
    private String lastAction;
    private Long roomId;

    @PostPersist
    public void onPostPersist(){

        /////////////////////////////////
        // RentCar 테이블 INSERT 시 수행
        /////////////////////////////////

        // 기본값 셋팅
        lastAction = "register";    // Insert는 항상 register
        status = "available";       // 최초 등록시 항상 이용가능
        roomId = 0L;                // 최초 등록시 항상 0

        // CarRegistered 이벤트 발행
        CarRegistered carRegistered = new CarRegistered();
        BeanUtils.copyProperties(this, carRegistered);
        carRegistered.publishAfterCommit();

    }

    @PostUpdate
    public void onPostUpdate(){

        //////////////////////////////////
        // RentCar 테이블 UPDATE 시 수행
        //////////////////////////////////

        System.out.println("lastAction : " + lastAction);

        if(lastAction.equals("modify")) {

            // CarModified 이벤트 발행
            CarModified carModified = new CarModified();
            BeanUtils.copyProperties(this, carModified);
            carModified.publishAfterCommit();
        }

        if(lastAction.equals("reserved")) {

            // CarReserved 이벤트 발행
            CarReserved carReserved = new CarReserved();
            BeanUtils.copyProperties(this, carReserved);
            carReserved.publishAfterCommit();
        }

        if(lastAction.equals("cancelled")) {

            // CarCancelled 이벤트 발행
            CarCancelled carCancelled = new CarCancelled();
            BeanUtils.copyProperties(this, carCancelled);
            carCancelled.publishAfterCommit();
        }

    }

    @PreRemove
    public void onPreRemove(){

        /////////////////////////////////
        // RentCar 테이블 Delete 전 수행
        /////////////////////////////////
        // carDeleted 이벤트 발행
        CarDeleted carDeleted = new CarDeleted();
        BeanUtils.copyProperties(this, carDeleted);
        carDeleted.publishAfterCommit();
    }

    public Long getCarId() {
        return carId;
    }
    public void setCarId(Long carId) {
        this.carId = carId;
    }
    public String getCarName() {
        return carName;
    }
    public void setCarName(String carName) {
        this.carName = carName;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getLastAction() {
        return lastAction;
    }
    public void setLastAction(String lastAction) {
        this.lastAction = lastAction;
    }
    public Long getRoomId() {
        return roomId;
    }
    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

}
