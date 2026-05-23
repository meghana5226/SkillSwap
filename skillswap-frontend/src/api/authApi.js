import axiosClient from "./axiosClient";

export const signupApi = async(data)=>{

    const response =
    await axiosClient.post(

        "/auth/register",
        data

    );

    return response.data;

};


export const loginApi = async(data)=>{

    const response =
    await axiosClient.post(

        "/auth/login",
        data

    );

    return response.data;

};