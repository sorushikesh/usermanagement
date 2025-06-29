package com.sorushi.invoice.management.usermanagement.dto;

import java.util.List;
import lombok.Builder;

@Builder
public class ResponseBody {

  private String status;
  private List<String> messages;
}
