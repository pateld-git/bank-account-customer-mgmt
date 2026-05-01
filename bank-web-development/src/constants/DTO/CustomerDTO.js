export class CustomerDTO {
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

    toPayload() {
        return {
            name: this.name,
            type: this.type,
            address: this.address,
        };
    }
}