package com.seowonn.mymap.domain.openApi.dto.siDo;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SiDoFeatureCollection {
  private List<SiDoFeature> features;
}
