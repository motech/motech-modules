package org.motechproject.openmrs19.tasks;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.openmrs19.domain.Encounter;
import org.motechproject.openmrs19.domain.EncounterType;
import org.motechproject.openmrs19.domain.Patient;
import org.motechproject.openmrs19.domain.Person;
import org.motechproject.openmrs19.domain.Provider;
import org.motechproject.openmrs19.domain.Relationship;
import org.motechproject.openmrs19.domain.RelationshipType;
import org.motechproject.openmrs19.service.OpenMRSEncounterService;
import org.motechproject.openmrs19.service.OpenMRSPatientService;
import org.motechproject.openmrs19.service.OpenMRSProviderService;
import org.motechproject.openmrs19.service.OpenMRSRelationshipService;
import org.springframework.core.io.ResourceLoader;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.motechproject.openmrs19.tasks.OpenMRSTasksConstants.BY_MOTECH_ID;
import static org.motechproject.openmrs19.tasks.OpenMRSTasksConstants.BY_PERSON_UUID;
import static org.motechproject.openmrs19.tasks.OpenMRSTasksConstants.BY_UUID;
import static org.motechproject.openmrs19.tasks.OpenMRSTasksConstants.MOTECH_ID;
import static org.motechproject.openmrs19.tasks.OpenMRSTasksConstants.PERSON_UUID;
import static org.motechproject.openmrs19.tasks.OpenMRSTasksConstants.RELATIONSHIP_TYPE_UUID;
import static org.motechproject.openmrs19.tasks.OpenMRSTasksConstants.UUID;

@RunWith(MockitoJUnitRunner.class)
public class OpenMRSTaskDataProviderTest {

    @Mock
    private OpenMRSEncounterService encounterService;

    @Mock
    private OpenMRSPatientService patientService;

    @Mock
    private OpenMRSProviderService providerService;

    @Mock
    private OpenMRSRelationshipService relationshipService;

    @Mock
    private ResourceLoader resourceLoader;

    private OpenMRSTaskDataProvider taskDataProvider;

    @Before
    public void setUp() {
        taskDataProvider = new OpenMRSTaskDataProvider(resourceLoader, encounterService, patientService,
                providerService, relationshipService);
    }

    @Test
    public void shouldReturnNullWhenClassIsNotSupported() {
        String className = "testClass";

        Object object = taskDataProvider.lookup(className, "lookupName", null);

        assertNull(object);
        verifyZeroInteractions(providerService);
    }

    @Test
    public void shouldReturnNullWhenWrongLookupNameForEncounter() {
        String className = Encounter.class.getSimpleName();

        Object object = taskDataProvider.lookup(className, "wrongLookupName", null);

        assertNull(object);
        verifyZeroInteractions(encounterService);
    }

    @Test
    public void shouldReturnNullWhenEmptyLookupFieldsForLookupGetEncounterByUuid() {
        String className = Encounter.class.getSimpleName();
        Map<String, String> lookupFields = new HashMap<>();

        when(encounterService.getEncounterByUuid(null, null)).thenReturn(null);

        Object object = taskDataProvider.lookup(className, BY_UUID, lookupFields);

        assertNull(object);
        verify(encounterService).getEncounterByUuid(null, null);
    }

    @Test
    public void shouldReturnNullWhenEncounterNotFoundForLookupGetEncounterByUuid() {
        String className = Encounter.class.getSimpleName();
        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(UUID, "4");

        when(encounterService.getEncounterByUuid(null, "4")).thenReturn(null);

        Object object = taskDataProvider.lookup(className, BY_UUID, lookupFields);

        assertNull(object);
        verify(encounterService).getEncounterByUuid(null, "4");
    }

    @Test
    public void shouldReturnEncounterForLookupGetEncounterByUuid() {
        String className = Encounter.class.getSimpleName();
        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(UUID, "5");

        Encounter encounter = new Encounter();
        encounter.setEncounterType(new EncounterType("encounterTypeTest"));

        when(encounterService.getEncounterByUuid(null, "5")).thenReturn(encounter);

        Object object = taskDataProvider.lookup(className, BY_UUID, lookupFields);

        assertEquals(encounter, object);
        verify(encounterService).getEncounterByUuid(null, "5");
    }

    @Test
    public void shouldReturnNullWhenWrongLookupNameForPatient() {
        String className = Patient.class.getSimpleName();

        Object object = taskDataProvider.lookup(className, "wrongLookupName", null);

        assertNull(object);
        verifyZeroInteractions(patientService);
    }

    @Test
    public void shouldReturnNullWhenEmptyLookupFieldsForLookupGetPatientByUuid() {
        String className = Patient.class.getSimpleName();
        Map<String, String> lookupFields = new HashMap<>();

        when(patientService.getPatientByUuid(null, null)).thenReturn(null);

        Object object = taskDataProvider.lookup(className, BY_UUID, lookupFields);

        assertNull(object);
        verify(patientService).getPatientByUuid(null, null);
    }

    @Test
    public void shouldReturnNullWhenEmptyLookupFieldsForLookupGetPatientByMotechId() {
        String className = Patient.class.getSimpleName();
        Map<String, String> lookupFields = new HashMap<>();

        when(patientService.getPatientByMotechId(null, null)).thenReturn(null);

        Object object = taskDataProvider.lookup(className, BY_MOTECH_ID, lookupFields);

        assertNull(object);
        verify(patientService).getPatientByMotechId(null, null);
    }

    @Test
    public void shouldReturnNullWhenPatientNotFoundForLookupGetPatientByUuid() {
        String className = Patient.class.getSimpleName();
        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(UUID, "4");

        when(patientService.getPatientByUuid(null, "4")).thenReturn(null);

        Object object = taskDataProvider.lookup(className, BY_UUID, lookupFields);

        assertNull(object);
        verify(patientService).getPatientByUuid(null, "4");
    }

    @Test
    public void shouldReturnNullWhenPatientNotFoundForLookupGetPatientByMotechId() {
        String className = Patient.class.getSimpleName();
        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(MOTECH_ID, "4");

        when(patientService.getPatientByMotechId(null, "4")).thenReturn(null);

        Object object = taskDataProvider.lookup(className, BY_MOTECH_ID, lookupFields);

        assertNull(object);
        verify(patientService).getPatientByMotechId(null, "4");
    }

    @Test
    public void shouldReturnPatientForLookupGetPatientByUuid() {
        String className = Patient.class.getSimpleName();
        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(UUID, "5");

        Patient patient = new Patient();
        patient.setMotechId("10");

        when(patientService.getPatientByUuid(null, "5")).thenReturn(patient);

        Object object = taskDataProvider.lookup(className, BY_UUID, lookupFields);

        assertEquals(patient, object);
        verify(patientService).getPatientByUuid(null, "5");
    }

    @Test
    public void shouldReturnPatientForLookupGetPatientByMotechId() {
        String className = Patient.class.getSimpleName();
        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(MOTECH_ID, "5");

        Patient patient = new Patient();
        patient.setUuid("10");

        when(patientService.getPatientByMotechId(null, "5")).thenReturn(patient);

        Object object = taskDataProvider.lookup(className, BY_MOTECH_ID, lookupFields);

        assertEquals(patient, object);
        verify(patientService).getPatientByMotechId(null, "5");
    }

    @Test
    public void shouldReturnNullWhenWrongLookupNameForProvider() {
        String className = Provider.class.getSimpleName();

        Object object = taskDataProvider.lookup(className, "wrongLookupName", null);

        assertNull(object);
        verifyZeroInteractions(providerService);
    }

    @Test
    public void shouldReturnNullWhenEmptyLookupFieldsForLookupGetProviderByUuid() {
        String className = Provider.class.getSimpleName();
        Map<String, String> lookupFields = new HashMap<>();

        when(providerService.getProviderByUuid(null, null)).thenReturn(null);

        Object object = taskDataProvider.lookup(className, BY_UUID, lookupFields);

        assertNull(object);
        verify(providerService).getProviderByUuid(null, null);
    }

    @Test
    public void shouldReturnNullWhenProviderNotFoundForLookupGetProviderByUuid() {
        String className = Provider.class.getSimpleName();
        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(UUID, "4");

        when(providerService.getProviderByUuid(null, "4")).thenReturn(null);

        Object object = taskDataProvider.lookup(className, BY_UUID, lookupFields);

        assertNull(object);
        verify(providerService).getProviderByUuid(null, "4");
    }

    @Test
    public void shouldReturnProviderForLookupGetProviderByUuid() {
        String className = Provider.class.getSimpleName();
        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(UUID, "5");

        Provider provider = new Provider();
        provider.setIdentifier("testIdentifier");

        when(providerService.getProviderByUuid(null, "5")).thenReturn(provider);

        Object object = taskDataProvider.lookup(className, BY_UUID, lookupFields);

        assertEquals(provider, object);
        verify(providerService).getProviderByUuid(null, "5");
    }

    @Test
    public void shouldReturnRelationshipForLookupGetRelationshipByTypeUuidAndPersonBUuid() {
        String className = Relationship.class.getSimpleName();
        String relationshipTypeUuid = "relationshipTypeUuid";
        String personUuid = "personUuid";

        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(RELATIONSHIP_TYPE_UUID, relationshipTypeUuid);
        lookupFields.put(PERSON_UUID, personUuid);

        Relationship expected = prepareRelationship();

        when(relationshipService.getByTypeUuidAndPersonUuid(eq(null), eq(relationshipTypeUuid), eq(personUuid)))
                .thenReturn(expected);

        Object object = taskDataProvider.lookup(className, BY_PERSON_UUID, lookupFields);

        verify(relationshipService).getByTypeUuidAndPersonUuid(eq(null), eq(relationshipTypeUuid), eq(personUuid));

        assertEquals(expected, object);
    }

    private Relationship prepareRelationship() {
        Relationship relationship = new Relationship();
        relationship.setUuid("relationShipUuid");
        relationship.setEndDate("endDate");
        relationship.setStartDate("startDate");

        Person personA = new Person();
        personA.setUuid("personAUuid");

        Person personB = new Person();
        personB.setUuid("personBUuid");

        RelationshipType type = new RelationshipType();
        type.setUuid("relationshipTypeUuid");

        relationship.setPersonA(personA);
        relationship.setPersonB(personB);
        relationship.setRelationshipType(type);

        return relationship;
    }
}
