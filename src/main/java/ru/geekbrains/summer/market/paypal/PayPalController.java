package ru.geekbrains.summer.market.paypal;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.geekbrains.summer.market.exceptions.ExecuteOperationException;
import ru.geekbrains.summer.market.services.OrderService;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping("/api/v1/paypal")
@Deprecated
public class PayPalController {
    @Value("${paypal.client-id}")
    private String clientId;

    @Value("${paypal.client-secret}")
    private String clientSecret;

    @Value("${paypal.mode}")
    private String mode;

    @Value("${paypal.redirect-root}")
    private String redirectRoot;

    private APIContext apiContext;
    private OrderService orderService;

    @Autowired
    public PayPalController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostConstruct
    public void init() {
        apiContext = new APIContext(clientId, clientSecret, mode);
    }

    @GetMapping("/buy/{orderId}")
    @ResponseBody
    public Payment buy(@PathVariable(name = "orderId") Long orderId, Principal principal) {
        try {
            ru.geekbrains.summer.market.model.Order order = orderService.findById(orderId);

            Payer payer = new Payer();
            payer.setPaymentMethod("paypal");
            RedirectUrls redirectUrls = new RedirectUrls();
            redirectUrls.setCancelUrl(redirectRoot + "/cancel");
            redirectUrls.setReturnUrl(redirectRoot + "/success/" + order.getId());

            Amount amount = new Amount();
            amount.setCurrency("RUB");
            amount.setTotal(order.getPrice().toString());

            Transaction transaction = new Transaction();
            transaction.setAmount(amount);
            transaction.setDescription("Покупка в Summer Market");

            List<Transaction> transactions = new ArrayList<>();
            transactions.add(transaction);

            Payment payment = new Payment();
            payment.setPayer(payer);
            payment.setRedirectUrls(redirectUrls);
            payment.setTransactions(transactions);
            payment.setIntent("sale");

            Payment doPayment = payment.create(apiContext);

            return doPayment;

//            Iterator<Links> links = doPayment.getLinks().iterator();
//
//            while (links.hasNext()) {
//                Links link = links.next();
//                if (link.getRel().equalsIgnoreCase("approval_url")) {
//                    return "redirect:" + link.getHref();
//                }
//            }
        } catch (Exception e) {
            throw new ExecuteOperationException("Невозможно выполнить платеж");
        }
    }

    @GetMapping("/success/{orderId}")
    @ResponseBody
    public String success(HttpServletRequest request, HttpServletResponse response, Model model, @PathVariable(name = "orderId") Long orderId) {
        try {
            String paymentId = request.getParameter("paymentId");
            String payerId = request.getParameter("PayerID");

            if (paymentId == null || paymentId.isEmpty() || payerId == null || payerId.isEmpty()) {
                return "redirect:/";
            }

            Payment payment = new Payment();
            payment.setId(paymentId);

            PaymentExecution paymentExecution = new PaymentExecution();
            paymentExecution.setPayerId(payerId);

            Payment executedPayment = payment.execute(apiContext, paymentExecution);

            if (executedPayment.getState().equals("approved")) {
                model.addAttribute("message", "Ваш заказ #" + orderId + " сформирован и оплачен");
            } else {
                model.addAttribute("message", "Что-то пошло не так при формировании заказа, попробуйте повторить операцию");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "paypal-result";
    }

    @GetMapping("/cancel")
    @ResponseBody
    public String cancel(Model model) {
        model.addAttribute("message", "Оплата заказа была отменена");
        return "paypal-result";
    }
}