package dataStorageModel;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class NominaExtractDTO {
    private final List<NominaEntry> nominainfo;
    private boolean foundData;
} 