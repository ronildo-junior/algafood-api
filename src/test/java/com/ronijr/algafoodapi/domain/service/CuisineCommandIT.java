package com.ronijr.algafoodapi.domain.service;

import com.ronijr.algafoodapi.core.AbstractTest;
import com.ronijr.algafoodapi.core.DataTest;
import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.exception.EntityRelationshipException;
import com.ronijr.algafoodapi.domain.exception.ValidationException;
import com.ronijr.algafoodapi.domain.model.Cuisine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CuisineCommandIT extends AbstractTest  {
	private final CuisineCommand commandService;
	private final DataTest testData;

	@Autowired
	CuisineCommandIT(CuisineCommand commandService, DataTest testData) {
		this.commandService = commandService;
		this.testData = testData;
	}

	@BeforeEach
	void setUp(){
		cleaner.clearTables();
		testData.createCuisineBaseData();
	}

	@Test
	void shouldSuccess_WhenCreateCuisineValid() {
		//given
		Cuisine cuisine = Cuisine.builder().name("Brasileira").build();
		//when
		cuisine = commandService.create(cuisine);
		//then
		assertThat(cuisine).isNotNull();
		assertThat(cuisine.getId()).isNotNull();
	}

	@Test
	void shouldSuccess_WhenDeleteCuisineNoRelationship() {
		Long cuisineId = (long) DataTest.CUISINE_RELATIONSHIP_BEGIN - 1;
		assertThatCode(() -> commandService.delete(cuisineId)).doesNotThrowAnyException();
	}

	@ParameterizedTest
	@MethodSource("blankStrings")
	void shouldFail_WhenCreateCuisineWithBlankName(final String name) {
		//given
		Cuisine cuisine = Cuisine.builder().name(name).build();
		//when
		var exception =	assertThrows(ValidationException.class, () -> commandService.create(cuisine));
		//then
		assertThat(exception).isNotNull();
	}

	@Test
	void shouldFail_WhenDeleteCuisineWithRelationship() {
		//given
		Long cuisineId = (long) DataTest.CUISINE_RELATIONSHIP_BEGIN;
		//when
		var exception =	assertThrows(EntityRelationshipException.class, () -> commandService.delete(cuisineId));
		//then
		assertThat(exception).isNotNull();
	}

	@Test
	void shouldFail_WhenDeleteCuisineNonExistent() {
		//given
		Long cuisineId = (long) DataTest.CUISINE_NON_EXISTENT_ID;
		//when
		var exception =	assertThrows(EntityNotFoundException.class, () -> commandService.delete(cuisineId));
		//then
		assertThat(exception).isNotNull();
	}
}