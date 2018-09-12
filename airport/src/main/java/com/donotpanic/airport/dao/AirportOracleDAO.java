package com.donotpanic.airport.dao;

import com.donotpanic.airport.domain.Engine.CommonServices;
import com.donotpanic.airport.domain.Engine.InvalidCoordinatesException;
import com.donotpanic.airport.domain.aircraft.AircraftFactory;
import com.donotpanic.airport.domain.aircraft.AircraftModel;
import com.donotpanic.airport.domain.airport.Airport;
import com.donotpanic.airport.domain.airport.AirportFactory;
import com.donotpanic.airport.domain.common.PrintService;
import com.donotpanic.airport.domain.location.CoordinateFactory;
import com.donotpanic.airport.domain.location.Coordinates;
import oracle.jdbc.OracleTypes;
import org.springframework.asm.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository("OracleDAO")
@DependsOn("oracleDBLocal")
class AirportOracleDAO implements AirportDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AirportOracleDAO(@Qualifier("oracleDBLocal")DriverManagerDataSource source){
        jdbcTemplate = new JdbcTemplate(source);
    }

    @Override
    public void registerAircraftModel(AircraftModel model) {

    }

    @Override
    public ArrayList<AircraftModel> getAllModels() {
        return null;
    }

    @Override
    public void registerNewAirport(Airport airport) {
        String sql =
                "merge into arp_airports a\n" +
                "  using (select ? as airport_name,\n" +
                "                ? as coordinate_id\n" +
                "           from dual) src\n" +
                "     on (a.airport_name = src.airport_name) \n" +
                "When matched then update\n" +
                "  set a.coordinate_id = src.coordinate_id\n" +
                "when not matched then insert (airport_id, airport_name, coordinate_id)\n" +
                "values (seq_airport_id.nextval, src.airport_name, src.coordinate_id)";
        if (airport.getCoordinates().getDbCoordainateId() <= 0){
            registerCoordinate(airport.getCoordinates());
        }

        jdbcTemplate.update(sql, airport.getAirportName(), airport.getCoordinates().getDbCoordainateId());
    }

    private static class AirportRowMapper implements RowMapper<Airport>{
        @Override
        public Airport mapRow(ResultSet resultSet, int i) throws SQLException{
            Airport airport = null;
            CommonServices commonServices = CommonServices.getCommonServices();
            try {
                airport = commonServices.getAirportFactory().getInstanceAirport(resultSet.getString("airport_name"));
            }catch (Exception e){
                if (e instanceof SQLException){
                    throw (SQLException) e;
                }else{
                    e.printStackTrace();
                }
            }
            /*fill another attributes*/
            try{
                commonServices.getGlobalEngine().registerGlobalObject(airport, commonServices.getCoordinateFactory()
                        .getCoordinates(resultSet.getDouble("coor_x"), resultSet.getDouble("coor_y"))
                        .setDbCoordainateId(resultSet.getInt("coordinate_id")));
            }catch (InvalidCoordinatesException e){
                e.printStackTrace();
            }

            return airport;
        }
    }

    @Override
    public Airport getAirportByName(String airportName) {
        String sql = "select * from arp_airports where airport_name = '?'";
        return jdbcTemplate.queryForObject(sql, new Object[]{airportName}, new AirportRowMapper());
    }

    @Override
    public ArrayList<Airport> getAllAirports() {
        String sql = "select s.*, c.coor_x, c.coor_y from arp_airports s join ARP_COORDINATES C on s.COORDINATE_ID = C.COORDINATE_ID";
        return new ArrayList<>(jdbcTemplate.query(sql, new AirportRowMapper()));
    }

    private static class CoordinateRowMapper implements RowMapper<Coordinates>{
        @Override
        public Coordinates mapRow(ResultSet resultSet, int i) throws SQLException {
            return CommonServices.getCommonServices().getCoordinateFactory()
                    .getCoordinates(resultSet.getDouble("coor_x"), resultSet.getDouble("coor_y"));
        }
    }

    private class CoordinateProc extends StoredProcedure{
        private CoordinateProc(){
            super(jdbcTemplate, "p_register_coordinate");
            setFunction(false);
            setParameters(new SqlInOutParameter("op_coordinate_id", OracleTypes.INTEGER), new SqlParameter("ip_coor_x", OracleTypes.NUMBER), new SqlParameter("ip_coor_y", OracleTypes.NUMBER));
            this.compile();
        }
    }

    private class CoordinateAllFunc extends StoredProcedure{
        private CoordinateAllFunc(){
            super(jdbcTemplate, "f_get_coordinates");
            setFunction(true);
            setParameters(new SqlOutParameter("out", OracleTypes.CURSOR));
            this.compile();
        }
    }

    @Override
    public int registerCoordinate(Coordinates coordinates) {
        Map resultSet = this.new CoordinateProc().execute(coordinates.getDbCoordainateId(), coordinates.getX(), coordinates.getY());
        coordinates.setDbCoordainateId((int)resultSet.get("op_coordinate_id"));
        return coordinates.getDbCoordainateId();
    }

    @Override
    public void printAllCoordinates() throws SQLException {
        ArrayList<Map> resultSet = (ArrayList<Map>) this.new CoordinateAllFunc().execute().get("out");
        int num = 1;
        for (Map i : resultSet){
            System.out.println("Row: " + num++);
            System.out.println("COORDINATE_ID: " + i.get("COORDINATE_ID"));
            System.out.println("COOR_X: " + i.get("COOR_X"));
            System.out.println("COOR_Y: " + i.get("COOR_Y"));
            System.out.println("<================================================>");
        }
    }
}
