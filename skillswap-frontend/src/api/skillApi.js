import axiosClient from "./axiosClient";

export const getAllSkills = async()=>{

    const response =
    await axiosClient.get(
        "/skills"
    );

    return response.data;

};


export const addSkill = async(data)=>{

    const response =
    await axiosClient.post(
        "/skills",
        data
    );

    return response.data;

};


export const searchSkills = async(keyword)=>{

    const response =
    await axiosClient.get(

        `/skills/search?keyword=${keyword}`

    );

    return response.data;

};


export const deleteSkill = async(id)=>{

    const response =
    await axiosClient.delete(
        `/skills/${id}`
    );

    return response.data;

};