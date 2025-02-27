import type { Asset } from "#/entity";
import apiClient from "../apiClient";

export enum AssetApi {
	Base = "/assets",
}

// Get all assets
const getAll = () => apiClient.get<Asset[]>({ url: AssetApi.Base });

// Get asset by ID
const getById = (id: string) => apiClient.get<Asset>({ url: `${AssetApi.Base}/${id}` });

// Get assets by portfolio ID
const getByPortfolioId = (portfolioId: string) =>
	apiClient.get<Asset[]>({ url: `${AssetApi.Base}/portfolio/${portfolioId}` });

export default {
	getAll,
	getById,
	getByPortfolioId,
};
