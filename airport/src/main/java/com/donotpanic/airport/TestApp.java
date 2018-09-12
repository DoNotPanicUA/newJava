package com.donotpanic.airport;

import com.donotpanic.airport.dao.AirportDAO;
import com.donotpanic.airport.domain.Engine.CommonServices;
import com.donotpanic.airport.domain.airport.Airport;
import com.donotpanic.airport.domain.airport.AirportFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by DoNotPanic-NB on 13.07.2018.
 */
public class TestApp {
    public static void main(String[] args) throws Throwable {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("ApplicationContext.xml");
        CommonServices.getCommonServices().setMainContext(applicationContext);
        //System.out.println(((AirportDAO)applicationContext.getBean("OracleDAO")).registerCoordinate(CommonServices.getCommonServices().getCoordinateFactory().getRandomCoordinates()));
        AirportDAO dao = applicationContext.getBean("OracleDAO", AirportDAO.class);
        AirportFactory airportFactory = applicationContext.getBean(AirportFactory.class);
        refreshAirportLocations(applicationContext);
    }

    private static void refreshAirportLocations(ApplicationContext applicationContext) throws Throwable{
        AirportDAO dao = applicationContext.getBean("OracleDAO", AirportDAO.class);
        AirportFactory airportFactory = applicationContext.getBean(AirportFactory.class);
        //System.out.println(applicationContext.getBean(Airport.class));
        for (Airport a : airportFactory.getAirports()){
            //CommonServices.getCommonServices().getGlobalEngine().registerAirport(a);
            dao.registerNewAirport(a);
        }

    }
}
