import axiosClient
from "./axiosClient";

export const loginApi=
(data)=>{

return axiosClient.post(

"/auth/login",
data

);

};


export const signupApi=
(data)=>{

return axiosClient.post(

"/auth/register",
data

);

};