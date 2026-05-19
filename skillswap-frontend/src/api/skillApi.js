import axiosClient from "./axiosClient";

export const getAllSkills=async()=>{

const response=
await axiosClient.get(
"/skills"
);

return response.data;

};


export const searchSkills=async(
keyword
)=>{

const response=
await axiosClient.get(

`/skills/search?keyword=${keyword}`

);

return response.data;

};