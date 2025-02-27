import type { FileRecord } from "#/entity";
import apiClient from "../apiClient";

export enum FileRecordApi {
	Base = "/files",
	Upload = "/files/upload",
}

// Get all file records
const getAll = () => apiClient.get<FileRecord[]>({ url: FileRecordApi.Base });

// Upload new file
const upload = (formData: FormData) =>
	apiClient.post<FileRecord>({
		url: FileRecordApi.Upload,
		data: formData,
		headers: {
			"Content-Type": "multipart/form-data",
		},
	});

export default {
	getAll,
	upload,
};
