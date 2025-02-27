import type { Client } from "#/entity"; // You'll need to define this type
import apiClient from "../apiClient";

export enum ClientApi {
	Base = "/clients",
}

// Get all clients
const getAll = () => apiClient.get<Client[]>({ url: ClientApi.Base });

// Get client by ID
const getById = (id: string) => apiClient.get<Client>({ url: `${ClientApi.Base}/${id}` });

export default {
	getAll,
	getById,
};
