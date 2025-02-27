import type { BillingTier } from "#/entity";
import apiClient from "../apiClient";

export enum BillingTierApi {
	Base = "/billing-tiers",
}

// Get all billing tiers
const getAll = () => apiClient.get<BillingTier[]>({ url: BillingTierApi.Base });

export default {
	getAll,
};
