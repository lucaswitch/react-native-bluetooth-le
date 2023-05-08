export class BondError extends Error {
    constructor(message) {
        super(message);
        // @ts-ignore
        this.name = this.constructor.name;
    }
}

