import axiosClient from "./axiosClient";

export const getProfile = async()=>{

    const response =
    await axiosClient.get(
        "/users/profile"
    );

    return response.data;

};


export const updateProfile = async(data)=>{

    const response =
    await axiosClient.put(

        "/users/profile",
        data

    );

    return response.data;

};