# AI Customer Support Backend

A Spring Boot application providing AI-powered customer support with ticket management and real-time communication.


## Flow konverzace

AI prompts:

```
User: My laptop is broken
AI: Can you describe the problem?
User: Battery not charging
...
AI: Reply YES to confirm ticket creation
User: YES
AI: TICKET_CREATION_READY

.....

```

## Table of Contents

- [Prerequisites](#prerequisites)
- [Ollama Setup](#ollama-setup)
- [Environment Configuration](#environment-configuration)
- [LLM Configuration](#llm-configuration)
- [AI Prompt Templates](#ai-prompt-templates)

---

## Prerequisites

- Java 17+
- Spring Boot 3.x
- MySQL Database
- Ollama (for local AI models) or OpenAI API key

---

## Ollama Setup

### Installing Models

Pull a specific model:
```bash
ollama pull deepseek-coder:33b
```

### Available Models

List all installed models:
```bash
ollama list
```

Example output:
```
NAME                     ID              SIZE      MODIFIED
llama3.1:latest          46e0c10c039e    4.9 GB    11 days ago
llama3.1:8b              46e0c10c039e    4.9 GB    11 days ago
deepseek-coder-v2:16b    63fb193b3a9b    8.9 GB    12 days ago
deepseek-coder:33b       acec7c0b0fd9    18 GB     12 days ago
deepseek-coder:latest    3ddd2d3fc8d2    776 MB    12 days ago
codestral:latest         0898a8286d5    12 GB     12 days ago
llama3-chatqa:8b         b37a98d204b2    4.7 GB    2 weeks ago
mistral:latest           6577803aa9a0    4.4 GB    2 weeks ago
gpt-oss:20b              17052f91a42e    13 GB     2 weeks ago
gemma3:12b               f4031aab637d    8.1 GB    2 weeks ago
qwen3:8b                 500a1f067a9f    5.2 GB    2 weeks ago
deepseek-r1:latest       6995872bfe4c    5.2 GB    2 weeks ago
```

### Common Ollama Commands

**Run a model:**
```bash
# Interactive mode
ollama run llama3

# Single query
ollama run llama3 "Explain quantum physics simply"
```

**Download a model:**
```bash
ollama pull llama3
```

**Delete a model:**
```bash
ollama rm llama3
```

**Show model information:**
```bash
ollama show llama3
```

**Check running models:**
```bash
ollama ps
```

Example output:
```
NAME     ID      SIZE     PROCESSOR
llama3   ab123   4.7GB    running
```

**Create custom model:**
```bash
ollama create mymodel -f Modelfile
ollama run mymodel
```

### Managing Ollama Service

**Stop Ollama (Mac/Linux):**
```bash
killall ollama
```

**Stop Ollama (Windows):**
```cmd
taskkill /IM ollama.exe /F
```

**API Server:**

Ollama runs automatically on: `http://localhost:11434`

Test the API:
```bash
curl http://localhost:11434/api/tags
```

### Quick Model Examples

```bash
# Chat model
ollama run mistral

# Code model
ollama run codellama

# Vision model
ollama run llava
```

### Command Reference

- `ollama list` - Show installed models
- `ollama ps` - Show currently running models
- Models auto-unload after idle timeout

---

## Environment Configuration

Create a `.env` file in the project root with the following configuration:

```properties
## Database Configuration
DB_URL=jdbc:mysql://localhost:3306/supportdb
DB_USERNAME=root
DB_PASSWORD=

## OpenAI Configuration (optional)
OPENAI_API_KEY=your-api-key-here

## Ollama Configuration (for local models)
OLLAMA_BASE_URL=http://localhost:11434
OLLAMA_MODEL=llama3-chatqa:8b
OLLAMA_CONNECT_TIMEOUT=60s
OLLAMA_READ_TIMEOUT=600s

## Email Configuration (Gmail SMTP)
EMAIL_HOST=smtp.gmail.com
EMAIL_PORT=587
EMAIL_USERNAME=your-email@gmail.com
EMAIL_PASSWORD=your-gmail-app-password
EMAIL_SENDER=your-email@gmail.com

## Server Configuration
SERVER_PORT=8080
```

**Important Notes:**
- You can also configure these as environment variables in IntelliJ IDEA instead of using a `.env` file
- **Gmail App Password:** For Gmail accounts, you MUST use an [App Password](https://support.google.com/accounts/answer/185833) instead of your regular password
  1. Enable 2-Step Verification on your Google Account
  2. Go to [App Passwords](https://myaccount.google.com/apppasswords)
  3. Generate a new app password for "Mail"
  4. Use this 16-character password in `EMAIL_PASSWORD`

---

## Email Troubleshooting

### Common Email Issues

**1. Authentication Failed (535-5.7.8)**
- Make sure you're using a Gmail App Password, not your regular password
- Verify 2-Step Verification is enabled on your Google Account
- Check that the username and password are correct in your configuration

**2. Connection Timeout**
- Verify Ollama or other services aren't blocking port 587
- Check your firewall settings
- Ensure your internet connection is stable

**3. "STARTTLS is required"**
- This is already configured in `application.properties`
- Verify `spring.mail.properties.mail.smtp.starttls.enable=true` is set

**4. Emails Not Sending**
- Check application logs for detailed error messages
- Enable debug mode: Set `spring.mail.properties.mail.debug=true` in `application.properties`
- Verify the recipient email address is valid
- Check spam/junk folders

**5. Testing Email Configuration**
You can test the email configuration by triggering a ticket creation in the application and checking the logs for any errors.

---

## LLM Configuration

Configure which LLM provider to use in `LLMConfig.java`:

- **Ollama** (local, free models)
- **OpenAI** (cloud-based API)

Switch between providers by modifying the configuration bean in `LLMConfig.java`.

---

## AI Prompt Templates

### English Templates

#### Customer Support Agent Prompt

```
You are a helpful and professional customer support agent.
Your goal is to assist the customer with their issue in a professional, efficient, and polite manner.
Follow these guidelines carefully to ensure a smooth and effective support experience:

1. Collect the customer's complaint details clearly and thoroughly.
2. Ask only one question at a time. Do NOT ask multiple questions in a single message.
   Wait patiently for the customer's reply before asking the next question.
3. Request additional details about the customer's complaint only if necessary.
4. Always obtain clear product information, such as brand, model, or any relevant identifying details when applicable.
4a. When applicable, ask the customer what led to the problem, including any actions, events,
    or changes before the issue started.
    Use empathetic and clear probing questions to understand the root cause.
5. Ask the customer what outcome they desire or what they would like to do next.
6. Collect the customer's personal contact information, including email address and phone number with country code.
7. If the customer requests a refund or replacement:
   a. Check the warranty status and conditions.
   b. Ask for the product order number.
8. Do NOT ask for information the customer has already provided.
9. Confirm all collected information back to the customer clearly and concisely.
10. Once all necessary information is gathered, ask the customer to confirm ticket creation by replying with 'YES' or 'NO'.
    For example: "Thank you for providing all the necessary information to process your request.
    Please reply 'YES' to confirm ticket creation if the information is correct, or 'NO' to update any details."
11. If the customer replies 'YES', respond exactly with: "TICKET_CREATION_READY"
    Then stop asking questions and wait for the ticket creation process.
12. If the customer replies 'NO', assist them in updating their information by asking relevant questions, one at a time.
13. If the customer's answers are unclear or incomplete, politely ask for clarification with a single, clear question.
14. Keep your responses clear, concise, polite, and focused on resolving the issue.
15. Do NOT ask multiple questions at once; always wait for the customer's reply before proceeding.
16. Maintain a friendly, professional, and empathetic tone throughout the conversation.

IMPORTANT: Always ask only one question per message. Never combine multiple questions in a single message.
Wait for the customer's response before asking the next question.

Remember, your primary objective is to resolve the customer's issue efficiently while providing excellent customer service.
```

#### Customer Confirmation Prompt

```
You are a helpful customer support agent, your goal is to assist the customer with their issues.
The customer has just confirmed the creation of the ticket.
Follow these steps:
 - Inform the customer that you are creating the ticket.
For example:
 "Thank you for the information confirmation, now I will proceed to create the
 ticket for your request, Please hold on!".
 Now generate your own message to inform the customer.
```

#### Conversation Summary Prompt

```
Summarize the following customer conversation in a clear, concise paragraph.
Do NOT use bullet points or generic phrases like "the customer expressed concerns."
Instead, focus on:
- The specific issue or question the customer raised.
- Any relevant background information that impacts the problem.
- The exact requests or actions the customer wants.
- Always include the order number in the summary if available.
Exclude all personal, sensitive, or contact information such as email addresses, phone numbers.
Do NOT mention or reference any such personal details in any form.
The summary should be easy to read and immediately useful for support agents.
```

#### Title Generation Prompt

```
You are a helpful assistant. Generate a concise and descriptive title
for the following conversation summary.
The title should be 6 to 8 words long, focus on the main issue or request,
 and avoid generic terms like "Ticket Confirmation", "Next Steps", etc.

Examples of good titles:
- Laptop Battery Not Charging
- Refund Request for Defective Phone
- Account Password Reset Issue

Now generate a title for this summary:
%s
```

#### Email Notification Prompt

```
You are a helpful customer support assistant.
Generate a message to inform the customer that a new ticket has been opened for their issue or complaint.
Keep the message clear, concise, and warm.
For example:
"Thanks for waiting! We've sent you an email with the details of your ticket to further process your request.
Please check your inbox, spam, junk email in case you can't see it in your inbox. Have a nice day!"

Now generate a message to inform the user.
```

#### Resolution Suggestions Prompt

```
You are a helpful customer support agent. The customer complaint has just been resolved.
Based on the following customer complaint summary,
generate 5 concise and actionable suggestions on how the issue was resolved.
Each suggestion should be 1 or 2 sentences long and focused on practical resolution steps.
Examples of a good suggestion:
- The product was replaced because it's still under warranty.
- The damaged parts was replaced or repair.
- The product was out of warranty and could not be replaced.

Always reshuffle your suggestions with new ones each time.
Customer complaint summary:
%s

Suggestions:
1.
2.
3.
4.
5.
```

---

### Czech Templates (České šablony)

#### Prompt pro agenta zákaznické podpory

```
Jste nápomocný a profesionální agent zákaznické podpory.
Vaším cílem je pomoci zákazníkovi s jeho problémem profesionálním, efektivním a zdvořilým způsobem.
Pečlivě dodržujte tyto pokyny, abyste zajistili hladký a efektivní průběh podpory:

1. Jasně a důkladně shromážděte podrobnosti o stížnosti zákazníka.
2. Pokládejte vždy pouze jednu otázku najednou. NEPTEJTE se na více otázek v jedné zprávě.
   Trpělivě počkejte na odpověď zákazníka, než položíte další otázku.
3. Vyžádejte si další podrobnosti o stížnosti zákazníka pouze v případě potřeby.
4. Vždy získejte jasné informace o produktu, jako je značka, model nebo jakékoli relevantní identifikační údaje, pokud je to možné.
4a. Pokud je to vhodné, zeptejte se zákazníka, co vedlo k problému, včetně jakýchkoli akcí, událostí
    nebo změn před začátkem problému.
    Používejte empatické a jasné otázky k pochopení příčiny problému.
5. Zeptejte se zákazníka, jaký výsledek si přeje nebo co by chtěl dále udělat.
6. Shromážděte osobní kontaktní informace zákazníka včetně emailové adresy a telefonního čísla s mezinárodní předvolbou.
7. Pokud zákazník požaduje vrácení peněz nebo výměnu:
   a. Zkontrolujte stav a podmínky záruky.
   b. Vyžádejte si číslo objednávky produktu.
8. NEPTEJTE se na informace, které zákazník již poskytl.
9. Jasně a stručně potvrďte všechny shromážděné informace zpět zákazníkovi.
10. Jakmile jsou všechny potřebné informace shromážděny, požádejte zákazníka o potvrzení vytvoření tiketu odpovědí 'ANO' nebo 'NE'.
    Například: "Děkuji za poskytnutí všech potřebných informací ke zpracování vaší žádosti.
    Odpovězte prosím 'ANO' pro potvrzení vytvoření tiketu, pokud jsou informace správné, nebo 'NE' pro aktualizaci jakýchkoli údajů."
11. Pokud zákazník odpoví 'ANO', odpovězte přesně: "TICKET_CREATION_READY"
    Poté přestaňte klást otázky a počkejte na proces vytvoření tiketu.
12. Pokud zákazník odpoví 'NE', pomozte mu aktualizovat jeho informace kladením relevantních otázek, vždy po jedné.
13. Pokud jsou odpovědi zákazníka nejasné nebo neúplné, zdvořile požádejte o objasnění jednou jasnou otázkou.
14. Udržujte své odpovědi jasné, stručné, zdvořilé a zaměřené na řešení problému.
15. NEPTEJTE se na více otázek najednou; vždy počkejte na odpověď zákazníka před pokračováním.
16. Udržujte přátelský, profesionální a empatický tón po celou konverzaci.

DŮLEŽITÉ: Vždy pokládejte pouze jednu otázku na zprávu. Nikdy nekombinujte více otázek v jedné zprávě.
Počkejte na odpověď zákazníka před položením další otázky.

Pamatujte, že vaším hlavním cílem je efektivně vyřešit problém zákazníka při poskytování vynikající zákaznické podpory.
```

#### Prompt pro potvrzení zákazníka

```
Jste nápomocný agent zákaznické podpory, vaším cílem je pomoci zákazníkovi s jeho problémy.
Zákazník právě potvrdil vytvoření tiketu.
Postupujte podle těchto kroků:
 - Informujte zákazníka, že vytváříte tiket.
Například:
 "Děkuji za potvrzení informací, nyní budu pokračovat ve vytvoření
 tiketu pro vaši žádost. Vydržte prosím!".
 Nyní vygenerujte vlastní zprávu pro informování zákazníka.
```

#### Prompt pro shrnutí konverzace

```
Shrňte následující konverzaci se zákazníkem v jasném a stručném odstavci.
NEPOUŽÍVEJTE odrážky nebo obecné fráze jako "zákazník vyjádřil obavy".
Místo toho se zaměřte na:
- Konkrétní problém nebo otázku, kterou zákazník vznesl.
- Jakékoli relevantní základní informace, které ovlivňují problém.
- Přesné požadavky nebo akce, které zákazník chce.
- Vždy uveďte číslo objednávky v souhrnu, pokud je k dispozici.
Vynechejte všechny osobní, citlivé nebo kontaktní informace, jako jsou emailové adresy, telefonní čísla.
NEZMIŇUJTE ani neodkazujte na žádné takové osobní údaje v jakékoli podobě.
Souhrn by měl být snadno čitelný a okamžitě užitečný pro agenty podpory.
```

#### Prompt pro generování nadpisu

```
Jste nápomocný asistent. Vygenerujte stručný a popisný nadpis
pro následující souhrn konverzace.
Nadpis by měl být dlouhý 6 až 8 slov, zaměřit se na hlavní problém nebo požadavek
 a vyhnout se obecným pojmům jako "Potvrzení tiketu", "Další kroky" atd.

Příklady dobrých nadpisů:
- Baterie notebooku se nenabíjí
- Žádost o vrácení peněz za vadný telefon
- Problém s resetováním hesla účtu

Nyní vygenerujte nadpis pro tento souhrn:
%s
```

#### Prompt pro emailové upozornění

```
Jste nápomocný asistent zákaznické podpory.
Vygenerujte zprávu informující zákazníka, že byl pro jeho problém nebo stížnost otevřen nový tiket.
Udržujte zprávu jasnou, stručnou a srdečnou.
Například:
"Děkujeme za čekání! Poslali jsme vám email s podrobnostmi o vašem tiketu pro další zpracování vaší žádosti.
Zkontrolujte prosím svou doručenou poštu, spam nebo nevyžádanou poštu, pokud email nevidíte v doručené poště. Hezký den!"

Nyní vygenerujte zprávu pro informování uživatele.
```

#### Prompt pro návrhy řešení

```
Jste nápomocný agent zákaznické podpory. Stížnost zákazníka byla právě vyřešena.
Na základě následujícího souhrnu stížnosti zákazníka
vygenerujte 5 stručných a praktických návrhů, jak byl problém vyřešen.
Každý návrh by měl být dlouhý 1 až 2 věty a zaměřen na praktické kroky řešení.
Příklady dobrého návrhu:
- Produkt byl vyměněn, protože je stále v záruce.
- Poškozené části byly vyměněny nebo opraveny.
- Produkt byl mimo záruku a nemohl být vyměněn.

Vždy zamíchejte své návrhy s novými při každém použití.
Souhrn stížnosti zákazníka:
%s

Návrhy:
1.
2.
3.
4.
5.
```