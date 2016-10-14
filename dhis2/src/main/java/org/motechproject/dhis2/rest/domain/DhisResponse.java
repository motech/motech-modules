package org.motechproject.dhis2.rest.domain;

/**
 * Base class for DHIS2 action response.
 */
public abstract class DhisResponse {
    private DhisStatus status;
    private ImportCountDto importCountDto;

    public DhisStatus getStatus() {
        return status;
    }

    public void setStatus(DhisStatus status) {
        this.status = status;
    }

    public ImportCountDto getImportCountDto() {
        return importCountDto;
    }

    public void setImportCountDto(ImportCountDto importCountDto) {
        this.importCountDto = importCountDto;
    }
}
