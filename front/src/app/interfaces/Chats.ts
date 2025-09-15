export interface Chats {
  id: number;
  content: string;
 authorFirstname?: string;
  authorLastname?: string;
  created_at: string;
  updated_at: string;
}
export interface ApiChatListResponse {
  content: Chats[];
  page?: unknown; 
}