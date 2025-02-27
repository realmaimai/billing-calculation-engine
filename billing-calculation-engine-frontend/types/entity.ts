import type { BasicStatus, PermissionType } from "./enum";

export interface UserToken {
	accessToken?: string;
	refreshToken?: string | null;
}

export interface UserInfo {
	id: string;
	email: string;
	username: string;
	password?: string;
	avatar?: string;
	role?: Role | null;
	status?: BasicStatus | null;
	permissions?: Permission[] | null;
}

export interface Organization {
	id: string;
	name: string;
	status: "enable" | "disable";
	desc?: string;
	order?: number;
	children?: Organization[];
}

export interface Permission {
	id: string;
	parentId: string;
	name: string;
	label: string;
	type: PermissionType;
	route: string;
	status?: BasicStatus;
	order?: number;
	icon?: string;
	component?: string;
	hide?: boolean;
	hideTab?: boolean;
	frameSrc?: URL;
	newFeature?: boolean;
	children?: Permission[];
}

export interface Role {
	id: string;
	name: string;
	label: string;
	status: BasicStatus;
	order?: number;
	desc?: string;
	permission?: Permission[];
}

export interface Client {
	clientId: string;
	clientName: string;
	province: string;
	country: string;
	billingTierId: string;
	totalAum: number;
	totalFee: number;
	effectiveFeeRate: number;
}

export interface BillingTier {
	tierId: string;
	portfolioAumMin: number;
	portfolioAumMax: number;
	feePercentage: number;
}

export interface Asset {
	date: string;
	portfolioId: string;
	assetId: string;
	assetValue: number;
	currency: string;
	createdAt: string;
	updatedAt: string;
	createdBy: string | null;
	updatedBy: string | null;
}

export interface FileRecord {
	uploadId: number;
	fileName: string;
	fileType: string;
	uploadDate: string;
	createdBy: string;
	fileSize: number;
	status: string;
	processingResult: string;
}

export interface Portfolio {
	portfolioId: string;
	clientId: string;
	portfolioCurrency: string;
	portfolioAum: number;
	portfolioFee: number;
}
