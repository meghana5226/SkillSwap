export type SkillType = "OFFERING" | "LEARNING";
export type ProficiencyLevel = "BEGINNER" | "INTERMEDIATE" | "ADVANCED" | "EXPERT";

export interface Skill {
  id: string;
  name: string;
  category: string | null;
}

export interface UserSkill {
  id: string;
  skillId: string;
  skillName: string;
  category: string | null;
  type: SkillType;
  proficiency: ProficiencyLevel | null;
}

export interface Profile {
  id: string;
  fullName: string;
  email: string;
  role: "STUDENT" | "MENTOR" | "ADMIN";
  bio: string | null;
  experienceLevel: string | null;
  githubUrl: string | null;
  linkedinUrl: string | null;
  portfolioUrl: string | null;
  resumeUrl: string | null;
  location: string | null;
  available: boolean;
  skills: UserSkill[];
}

export interface UpdateProfilePayload {
  bio?: string;
  experienceLevel?: string;
  githubUrl?: string;
  linkedinUrl?: string;
  portfolioUrl?: string;
  location?: string;
  available?: boolean;
}

export interface AddSkillPayload {
  skillName: string;
  category?: string;
  type: SkillType;
  proficiency?: ProficiencyLevel;
}
