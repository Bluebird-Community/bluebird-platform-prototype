package org.bluebird.platform.engine.alarms.definition;

final class AlarmActions {
//    private static final AlarmAction NOOP = new AlarmAction() {
//        @Override
//        public String getDescription() {
//            return "NOOP";
//        }
//
//        @Override
//        public void apply(AlarmContext context) {
//
//        }
//    };
//
//    private AlarmActions() {
//
//    }
//
//    // TODO MVR can be removed ...
//    static AlarmAction raise(String reductionKeyTemplate) {
//        return new AlarmAction() {
//            @Override
//            public String getDescription() {
//                return "alarm.setConsolidationKey('%s')".formatted(reductionKeyTemplate);
//            }
//
//            @Override
//            public void apply(AlarmContext context) {
//                final var renderer = new EventDefinitionRenderer(new EventRenderContext(context.getEvent()));
//                final var reductionKey = renderer.render(reductionKeyTemplate);
//                context.getAlarm().setConsolidationKey(reductionKey);
//            }
//        };
//    }
//
//    static AlarmAction clear() {
//        return noop();
//    }
//
//    static AlarmAction noop() {
//        return AlarmActions.NOOP;
//    }

}
