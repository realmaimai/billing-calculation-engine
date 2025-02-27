import apiClient from "../apiClient";
import type { Portfolio } from "#/entity";

export enum PortfolioApi {
    Base = "/portfolios"
}

// Get all portfolios
const getAll = () => 
    apiClient.get<Portfolio[]>({ url: PortfolioApi.Base });

// Get portfolio by ID
const getById = (id: string) => 
    apiClient.get<Portfolio>({ url: `${PortfolioApi.Base}/${id}` });

// Get portfolios by client ID
const getByClientId = (clientId: string) => 
    apiClient.get<Portfolio[]>({ url: `${PortfolioApi.Base}/client/${clientId}` });

export default {
    getAll,
    getById,
    getByClientId,
};
