import { defineCollection, z } from 'astro:content';
import { glob } from 'astro/loaders';
export const collections = { lessons: defineCollection({loader:glob({pattern:'**/*.mdx',base:'./src/content/lessons'}),schema:z.object({title:z.string(),module:z.string(),summary:z.string(),group:z.string().optional(),order:z.number(),version:z.string().default('Java 8+'),prerequisites:z.array(z.string()).default([]),advanced:z.boolean().default(false)})}) };
