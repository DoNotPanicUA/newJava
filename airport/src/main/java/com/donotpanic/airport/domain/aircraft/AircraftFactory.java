package com.donotpanic.airport.domain.aircraft;

import com.donotpanic.airport.dao.HibernateUtils;
import com.donotpanic.airport.domain.Engine.GlobalEngine;
import com.donotpanic.airport.domain.aircraft.aircraft_actions.*;
import com.donotpanic.airport.domain.airport.Airport;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

//import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Service
public class AircraftFactory {
    private HashMap<String, AircraftModel> knownPassengerAircraftModels = new HashMap<>();
    private GlobalEngine globalEngine;

    public Set<String> getListOfPassengerModels(){
        return knownPassengerAircraftModels.keySet();
    }

    AircraftModel getPassengerAircraftModel(String modelName){
        return knownPassengerAircraftModels.get(modelName);
    }

    private void updDBAircraftModel(AircraftModel model){
        Session session = HibernateUtils.getInstance().getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try{
            session.merge(model);
            tx.commit();
            session.close();
        }catch(HibernateException e){
            System.out.println(e);
        }
        session.close();

    }

    private void addNewPassengerAircraftModel(String modelName, double flySpeed, double groundSpeed, AircraftColor color){
        AircraftModel existingModel = knownPassengerAircraftModels.get(modelName);
        if (existingModel != null){
            existingModel.setAircraftFlySpeed(flySpeed);
            existingModel.setAircraftGroundSpeed(groundSpeed);
            existingModel.setPlaneColor(color);
            updDBAircraftModel(existingModel);
        }else{
            AircraftModel newModel = new AircraftModel(modelName, flySpeed, groundSpeed);
            newModel.setPlaneColor(color);
            knownPassengerAircraftModels.put(modelName, newModel);
            updDBAircraftModel(newModel);
        }
    }

    @Bean(name = "AircraftFactory")
    @Autowired
    @DependsOn("GlobalEngine")
    AircraftFactory getAircraftFactory(GlobalEngine globalEngine){
        return new AircraftFactory(globalEngine);
    }

    private AircraftFactory(GlobalEngine globalEngine){
        this.globalEngine = globalEngine;
        fetchKnownModelsFromDB(knownPassengerAircraftModels);
//        addNewPassengerAircraftModel("Boeing 737-100", 2.23, 0.05, new AircraftColor(1, 255,0,0));
//        addNewPassengerAircraftModel("Boeing 747-8", 2.75, 0.04, new AircraftColor(2, 0,255,0));
//        addNewPassengerAircraftModel("AN-2", 0.53, 0.09, new AircraftColor(3, 0,0,255));
    }

    private void fetchKnownModelsFromDB(HashMap<String, AircraftModel> knownAircraftModels){
        Session session = HibernateUtils.getInstance().getSessionFactory().openSession();
//        Transaction tx = session.beginTransaction();
//        tx.begin();
        knownAircraftModels.clear();
        List<AircraftModel> result = session.createQuery("from AircraftModel").getResultList();
        for (AircraftModel model : result) {
            System.out.println(model.toString());
            knownAircraftModels.put(model.getModelName(), model);
        }
    }

    private AircraftActionPlan getPassengerPlanePlan(){
        AircraftActionPlan plan = new AircraftActionPlan(){};
        GlobalEngine engine = globalEngine;

        AircraftAction AA_M2A = new AircraftAction_Move2Airstrip().setEngine(engine);
        AircraftAction AA_RA  = new AircraftAction_RequestAirstrip().setEngine(engine);

        // 1st step
        plan.addLinkedAction(plan, AA_M2A);
        plan.addLinkedAction(plan, AA_RA);

        AircraftAction AA_TO   = new AircraftAction_TakeOff().setEngine(engine);
        AircraftAction AA_F2AP = new AircraftAction_Fly2Airport().setEngine(engine);
        AircraftAction AA_FC   = new AircraftAction_FlyCircle().setEngine(engine).setActionPriority(5/*low*/);
        AircraftAction AA_RP2  = new AircraftAction_RequestLandPark().setEngine(engine);
        AircraftAction AA_RA2  = new AircraftAction_RequestAirstrip().setEngine(engine);
        AircraftAction AA_F2AS = new AircraftAction_Fly2Airstrip().setEngine(engine);


        // 2nd step
        plan.addLinkedAction(AA_M2A, AA_TO);
        plan.addLinkedAction(AA_TO, AA_F2AP);
        plan.addLinkedAction(AA_F2AP, AA_RA2);
        plan.addLinkedAction(AA_F2AP, AA_RP2);
        plan.addLinkedAction(AA_F2AP, AA_FC);
        plan.addLinkedAction(AA_F2AP, AA_F2AS);

        // 3rd step
        AircraftAction AA_L    = new AircraftAction_Landing().setEngine(engine);
        AircraftAction AA_M2P  = new AircraftAction_Move2Park().setEngine(engine);

        plan.addLinkedAction(AA_F2AS, AA_L);
        plan.addLinkedAction(AA_L, AA_M2P);

        return plan;

    }

    public Plane getPassengerPlane(String name, Airport airport, String aircraftModelName) throws Throwable{
        AircraftModel model = getPassengerAircraftModel(aircraftModelName);
        Plane newPlane = new Plane(name + " (" + aircraftModelName + " ver." +model.getModelVersion() + ")",
                model.getAircraftFlySpeed(), model.getAircraftGroundSpeed(), this.globalEngine);
        newPlane.iniByAirport(airport);
        newPlane.setActionPlan(getPassengerPlanePlan());
        newPlane.setColor(model.getColor());
        new Thread(newPlane).start();
        return newPlane;
    }
}
