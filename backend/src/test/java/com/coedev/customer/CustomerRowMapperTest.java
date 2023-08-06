package com.coedev.customer;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerRowMapperTest {


    @Test
    void mapRow() throws SQLException {
        //GIVEN
        CustomerRowMapper customerRowMapper = new CustomerRowMapper();
        ResultSet resultSet = mock(ResultSet.class);
        Mockito.when(resultSet.getInt("id")).thenReturn(1);
        Mockito.when(resultSet.getInt("age")).thenReturn(19);
        Mockito.when(resultSet.getString("name")).thenReturn("jamila");
        Mockito.when(resultSet.getString("email")).thenReturn("jamila@gmail.com");
        //WHEN
        Customer actual = customerRowMapper.mapRow(resultSet, 1);

        //THEN
        Customer expected = new Customer(1, "jamila", "jamila@gmail.com", 19);

        Assertions.assertThat(actual).isEqualTo(expected);
    }
}