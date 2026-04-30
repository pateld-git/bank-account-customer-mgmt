export class CustomerDTO {
    constructor(formData) {
        this.name = formData.name || "no input";
        this.type = formData.type || "no input";
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