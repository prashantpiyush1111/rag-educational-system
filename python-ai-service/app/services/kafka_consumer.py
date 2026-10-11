"""Kafka integration is intentionally not part of this deployment.
The current architecture uses synchronous Java-to-FastAPI requests and
does not require an event consumer. This module is retained for import
compatibility only and does not start background threads or connections.
"""