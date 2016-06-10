package org.motechproject.openmrs.resource.impl;

import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.config.ConfigDummyData;
import org.motechproject.openmrs.domain.Encounter;
import org.motechproject.openmrs.domain.EncounterListResult;
import org.motechproject.openmrs.domain.Person;
import org.motechproject.openmrs.resource.EncounterResource;
import org.motechproject.openmrs.util.JsonUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestOperations;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class EncounterResourceImplTest extends AbstractResourceImplTest {

    private static final String ENCOUNTER_BY_PATIENT_RESPONSE_JSON = "json/encounter/encounter-by-patient-response.json";
    private static final String ENCOUNTER_RESPONSE_JSON = "json/encounter/encounter-response.json";
    private static final String ENCOUNTER_WITH_PROVIDER_LIST_JSON = "json/encounter/encounter-with-provider-list-response.json";
    private static final String CREATE_ENCOUNTER_JSON = "json/encounter/encounter-create.json";

    @Mock
    private RestOperations restOperations;

    @Captor
    private ArgumentCaptor<HttpEntity<String>> requestCaptor;

    private EncounterResource encounterResource;

    private Config config;

    @Before
    public void setUp() {
        initMocks(this);
        encounterResource = new EncounterResourceImpl(restOperations);
        config = ConfigDummyData.prepareConfig("one");
    }

    @Test
    public void shouldCreateEncounter() throws Exception {
        Encounter encounter = prepareEncounter();
        URI url = config.toInstancePath("/encounter?v=full");

        when(restOperations.exchange(eq(url), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(getResponse(ENCOUNTER_RESPONSE_JSON));

        Encounter created = encounterResource.createEncounter(config, encounter);

        verify(restOperations).exchange(eq(url), eq(HttpMethod.POST), requestCaptor.capture(), eq(String.class));

        assertThat(created, equalTo(encounter));
        assertThat(requestCaptor.getValue().getHeaders(), equalTo(getHeadersForPost(config)));
        assertThat(JsonUtils.readJson(requestCaptor.getValue().getBody(), JsonObject.class),
                equalTo(readFromFile(CREATE_ENCOUNTER_JSON, JsonObject.class)));
    }

    @Test
    public void shouldCreateEncounterWithProviderList() throws Exception {
        Encounter encounter = prepareEncounter();
        URI url = config.toInstancePath("/encounter?v=full");

        when(restOperations.exchange(eq(url), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(getResponse(ENCOUNTER_WITH_PROVIDER_LIST_JSON));

        Encounter created = encounterResource.createEncounter(config, encounter);

        verify(restOperations).exchange(eq(url), eq(HttpMethod.POST), requestCaptor.capture(), eq(String.class));

        //Since OpenMRS v1.10 createEncounter request needs providers as a single object and
        //response returns provider as a list.
        List<Person> providerList = new ArrayList<>();
        Person provider = new Person("PPR");
        providerList.add(provider);
        encounter.setEncounterProviders(providerList);

        assertThat(created, equalTo(encounter));
        assertThat(requestCaptor.getValue().getHeaders(), equalTo(getHeadersForPost(config)));
        assertThat(JsonUtils.readJson(requestCaptor.getValue().getBody(), JsonObject.class),
                equalTo(readFromFile(CREATE_ENCOUNTER_JSON, JsonObject.class)));
    }

    @Test
    public void shouldQueryAllEncountersBYPatientId() throws Exception {
        String patientId = "200";
        URI url = config.toInstancePathWithParams("/encounter?patient={id}&v=full", patientId);

        when(restOperations.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(getResponse(ENCOUNTER_BY_PATIENT_RESPONSE_JSON));

        EncounterListResult result = encounterResource.queryForAllEncountersByPatientId(config, patientId);

        verify(restOperations).exchange(eq(url), eq(HttpMethod.GET), requestCaptor.capture(), eq(String.class));

        assertThat(result, equalTo(readFromFile(ENCOUNTER_BY_PATIENT_RESPONSE_JSON, EncounterListResult.class)));
        assertThat(requestCaptor.getValue().getHeaders(), equalTo(getHeadersForGet(config)));
        assertThat(requestCaptor.getValue().getBody(), nullValue());
    }

    public Encounter prepareEncounter() throws Exception {
        return (Encounter) readFromFile(ENCOUNTER_RESPONSE_JSON, Encounter.class);
    }
}
