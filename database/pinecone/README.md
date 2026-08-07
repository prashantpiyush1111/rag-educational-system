# Pinecone (Managed Vector DB — SaaS)

Pinecone is a fully-managed vector database — there is no local Docker setup
because it runs entirely in the cloud.

## Setup

1. Create a free account at https://www.pinecone.io
2. Create an index:
   - Name: `educational-documents`
   - Dimensions: `384` (matches `all-MiniLM-L6-v2` embedding model)
   - Metric: `cosine`
3. Copy your API key from the Pinecone dashboard.
4. Add it to `python-ai-service/.env`: