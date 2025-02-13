package be.glever.antplus.common.datapage.registry;

import be.glever.ant.util.ByteUtils;
import be.glever.antplus.common.datapage.AbstractAntPlusDataPage;
import be.glever.util.logging.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * A DataPageRegistry holds Ant+ {@link AbstractAntPlusDataPage}s for a specific device.
 * As some datapages are specific for a given device and others are common ( defined in {@link CommonDataPageRegistry} ),
 * this base class allows grouping multiple registries together for a given Ant+ device type.
 */
public abstract class AbstractDataPageRegistry {

    private Map<Byte, DataPageBuilder> registry = new HashMap<>();

    private static final Log LOG = Log.getLogger(AbstractDataPageRegistry.class);

    public AbstractDataPageRegistry() {
    }

    public AbstractDataPageRegistry(AbstractDataPageRegistry... sourceRegistries) {
        for (AbstractDataPageRegistry src : sourceRegistries) {
            addAllFromRegistry(src);
        }
    }

    private void addAllFromRegistry(AbstractDataPageRegistry src) {
        for (Map.Entry<Byte, DataPageBuilder> entry : src.registry.entrySet()) {
            add(entry.getKey(), entry.getValue());
        }
    }

    public AbstractAntPlusDataPage constructDataPage(byte[] payLoadBytes) {
        DataPageBuilder dpBuilder = registry.get(payLoadBytes[0]);
        if (dpBuilder == null) {
            int messageId = ByteUtils.toInt(payLoadBytes[0]);
            if (messageId >= 0x70 && messageId <= 0x7F) {
                LOG.info(() -> "Manufacturer Specific for Toggle Bit Device Profiles data page " + messageId + " not yet supported");
            } else if (messageId >= 0xE0 && messageId <= 0xFF) {
                LOG.info(() -> "Manufacturer Specific data page " + messageId + " not yet supported");
            } else {
                LOG.info(() -> "Data page " + payLoadBytes[0] + " not yet supported");
            }
            return null;
        }

        return dpBuilder.construct(payLoadBytes);
    }

    protected void add(byte pageNumber, DataPageBuilder builder) {
        if (this.registry.containsKey(pageNumber)) {
            // Ignore duplicate pages. Needed for ComprehensiveDataPageRegistry
            LOG.trace(() -> "Duplicate DataPage defined in registry: " + pageNumber);
            return;
        }
        this.registry.put(pageNumber, builder);
    }

    public interface DataPageBuilder {
        AbstractAntPlusDataPage construct(byte[] bytes);
    }
}
