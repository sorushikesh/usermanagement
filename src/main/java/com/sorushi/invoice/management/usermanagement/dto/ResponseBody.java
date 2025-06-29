package com.sorushi.invoice.management.usermanagement.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ResponseBody {

  private String status;
  private List<String> messages;
}
