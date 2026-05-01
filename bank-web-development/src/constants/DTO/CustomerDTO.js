/**
 * Data Transfer Object for Customer information.
 */
export class CustomerDTO {
    /**
     * Creates an instance of CustomerDTO.
     * @param {Object} formData - The raw form data.
     * @param {string} [formData.name] - The name of the customer.
     * @param {string} [formData.type] - The type of customer (e.g., PERSON, COMPANY).
     * @param {string|number} formData.streetNumber - The street number of the address.
     * @param {string} formData.city - The city of the address.
     * @param {string} formData.province - The province or state.
     * @param {string} formData.postalCode - The postal or zip code.
     */
    constructor(formData) {
        this.name = formData.name || "known-by-db";
        this.type = formData.type || "known-by-db";
        this.address = {
            streetNumber: formData.streetNumber,
            city: formData.city,
            province: formData.province,
            postalCode: formData.postalCode,
        };
    }

    /**
     * Converts the DTO instance to a plain object for API payloads.
     * @returns {Object} The customer payload.
     */
    toPayload() {
        return {
            name: this.name,
            type: this.type,
            address: this.address,
        };
    }
}