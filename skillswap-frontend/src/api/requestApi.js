import axiosClient from "./axiosClient";

export const getRequests=async()=>{

const response=
await axiosClient.get(
"/requests"
);

return response.data;

};

export const sendRequest=async(skillId)=>{

const response=
await axiosClient.post(
`/requests/${skillId}`
);

return response.data;

};

export const acceptRequest=async(id)=>{

const response=
await axiosClient.put(
`/requests/${id}/accept`
);

return response.data;

};

export const rejectRequest=async(id)=>{

const response=
await axiosClient.put(
`/requests/${id}/reject`
);

return response.data;

};