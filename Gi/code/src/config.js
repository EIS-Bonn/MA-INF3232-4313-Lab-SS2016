
export const prefixes = {
  geo: 'http://www.w3.org/2003/01/geo/wgs84_pos#',
  ngeo: 'http://geovocab.org/geometry#',
  scor: 'http://purl.org/eis/vocab/scor#',
  ex: 'http://example.org/',
};

export const processMetrics = {
  DeliverStockedProduct: `
    <div id="metricSelection" class="ui selection dropdown">
      <input type="hidden" name="metricInput" value="DELIVERY_PERFORMANCE">
      <input type="hidden" name="metricInput" value="DELIVERY_IN_FULL">
      <i class="dropdown icon"></i>
      <div class="default text">Metric</div>
      <div class="menu">
        <div class="item" data-value="DELIVERY_PERFORMANCE">Delivery Performance</div>
        <div class="item" data-value="DELIVERY_IN_FULL">Delivery In Full</div>
      </div>
    </div>

  `,
  SourceMakeToOrderProduct: `
    <div id="metricSelection" class="ui selection dropdown">
      <input type="hidden" name="metricInput" value="SOURCING_COST">
      <i class="dropdown icon"></i>
      <div class="default text">Metric</div>
      <div class="menu">
        <div class="item" data-value="SOURCING_COST">Sourcing Cost</div>
      </div>
    </div>

  `,
  MakeToOrder: `
    <div id="metricSelection" class="ui selection dropdown">
      <input type="hidden" name="metricInput" value="PRODUCTION_COST">
      <i class="dropdown icon"></i>
      <div class="default text">Metric</div>
      <div class="menu">
        <div class="item" data-value="PRODUCTION_COST">Production Cost</div>
      </div>
    </div>

  `,
};
