# SimulationController compile fix

Fixed the DTO/entity type mismatch in `backend/src/main/java/com/netguard/ai/controllers/SimulationController.java`.

The controller now:
- receives `DeviceDTO` values from `DeviceService`
- reloads the corresponding `Device` entity through `DeviceRepository`
- receives `AlertDTO` from `AlertService`
- reloads the created `Alert` entity through `AlertRepository`

This keeps the existing service/API design intact while allowing RCA and incident creation to continue using JPA entities.
