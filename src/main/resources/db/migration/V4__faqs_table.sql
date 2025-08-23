-- Create FAQs table
CREATE TABLE faqs (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      question VARCHAR(500) NOT NULL,
                      answer TEXT NOT NULL,
                      category VARCHAR(100),
                      tags VARCHAR(255),
                      is_active BOOLEAN DEFAULT TRUE,
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                      created_by_admin_id BIGINT,
                      view_count INT DEFAULT 0,
                      INDEX idx_category (category),
                      INDEX idx_is_active (is_active),
                      INDEX idx_question (question),
                      FULLTEXT INDEX idx_search (question, answer, tags)
);

-- Insert sample FAQ data for Codeline and Rihal (Oman startups)
INSERT INTO faqs (question, answer, category, tags) VALUES
-- Codeline FAQs
('What is Codeline and what services do you offer?',
 'Codeline is a leading technology company in Oman specializing in software development, digital transformation, and IT consulting. We offer custom software solutions, mobile app development, web development, cloud services, and digital consulting to help businesses modernize their operations.',
 'About Company', 'codeline, services, software development, digital transformation'),

('How can I apply for a job at Codeline?',
 'You can apply for positions at Codeline by visiting our careers page on our website or sending your CV to careers@codeline.om. We regularly post open positions for software developers, project managers, UI/UX designers, and other tech roles. We welcome fresh graduates and experienced professionals.',
 'Careers', 'codeline, jobs, careers, hiring, employment'),

('What technologies does Codeline specialize in?',
 'Codeline works with modern technologies including Java, Python, React, Angular, Node.js, .NET, mobile development (iOS/Android), cloud platforms (AWS, Azure), and database technologies. We also specialize in AI/ML solutions and blockchain development.',
 'Technical', 'codeline, technology, programming, java, react, cloud, AI'),

('Does Codeline provide internship opportunities for students?',
 'Yes, Codeline offers internship programs for computer science and IT students from Omani universities. Our internship program provides hands-on experience in real projects, mentorship from senior developers, and potential full-time opportunities upon graduation.',
 'Careers', 'codeline, internship, students, training, mentorship'),

('What is Codeline''s approach to digital transformation?',
 'Codeline helps organizations undergo digital transformation by analyzing current processes, identifying automation opportunities, implementing modern software solutions, and training staff. We focus on improving efficiency, reducing costs, and enhancing customer experience through technology.',
 'Services', 'codeline, digital transformation, automation, modernization'),

-- Rihal FAQs
('What is Rihal and what makes it unique in Oman?',
 'Rihal is an innovative technology startup in Oman focused on creating cutting-edge software solutions and fostering the local tech ecosystem. We specialize in fintech, e-commerce, and enterprise solutions while supporting the growth of Oman''s digital economy.',
 'About Company', 'rihal, startup, innovation, fintech, oman technology'),

('How can I join Rihal as a software developer?',
 'To join Rihal, visit our website at rihal.om or follow us on LinkedIn for job postings. We look for passionate developers with experience in modern frameworks, strong problem-solving skills, and enthusiasm for innovation. We offer competitive packages and growth opportunities.',
 'Careers', 'rihal, developer jobs, software engineering, hiring'),

('What products and solutions does Rihal develop?',
 'Rihal develops fintech solutions, e-commerce platforms, business management systems, and mobile applications. We also create custom software for enterprises and startups, focusing on scalable, secure, and user-friendly solutions that meet local market needs.',
 'Products', 'rihal, fintech, ecommerce, mobile apps, enterprise solutions'),

('Does Rihal support other startups in Oman?',
 'Yes, Rihal actively supports the Omani startup ecosystem through mentorship programs, technical consulting, and collaboration opportunities. We believe in fostering innovation and helping other startups succeed through knowledge sharing and partnership.',
 'Community', 'rihal, startup support, mentorship, oman ecosystem, collaboration'),

('What is Rihal''s vision for Oman''s technology sector?',
 'Rihal envisions Oman as a leading technology hub in the region, with thriving startups, skilled local talent, and innovative solutions that serve both local and international markets. We work towards building sustainable tech businesses and contributing to Oman Vision 2040.',
 'Vision', 'rihal, oman vision 2040, technology hub, innovation, sustainability'),

-- General Tech/Startup FAQs for both companies
('How do I report a technical issue or bug?',
 'You can report technical issues through our support portal, email our technical team, or use the contact form on our website. Please provide detailed information about the issue, steps to reproduce it, and any error messages you encountered.',
 'Support', 'technical support, bug report, issues, troubleshooting'),

('What are the working hours and culture at Omani tech startups?',
 'Most Omani tech startups, including Codeline and Rihal, maintain flexible working hours (typically 8 AM to 5 PM) with options for remote work. The culture emphasizes innovation, collaboration, continuous learning, and work-life balance.',
 'Culture', 'working hours, company culture, remote work, flexibility'),

('How can I stay updated with the latest news from Omani tech companies?',
 'Follow Codeline and Rihal on social media platforms (LinkedIn, Twitter, Instagram), subscribe to their newsletters, visit their websites regularly, and attend local tech events and meetups where they often participate and share updates.',
 'Updates', 'news updates, social media, newsletters, tech events, community'),

('What programming languages should I learn to work at Omani startups?',
 'Popular languages in Omani tech companies include JavaScript (React, Node.js), Python, Java, C#, Swift/Kotlin for mobile development, and SQL for databases. Also valuable are cloud technologies (AWS, Azure) and modern development practices like DevOps and Agile.',
 'Learning', 'programming languages, skills, javascript, python, mobile development'),

('How do Omani tech startups contribute to Vision 2040?',
 'Omani tech startups contribute to Vision 2040 by creating jobs for local talent, developing innovative solutions for various sectors, promoting digital transformation, attracting foreign investment, and positioning Oman as a regional technology and innovation hub.',
 'Vision 2040', 'oman vision 2040, economic diversification, innovation, job creation');

-- Add some additional FAQs with different categories
INSERT INTO faqs (question, answer, category, tags) VALUES
                                                        ('What payment methods do you accept?',
                                                         'We accept various payment methods including credit/debit cards (Visa, MasterCard), bank transfers, and digital wallets. For enterprise clients, we also offer invoice-based payment terms.',
                                                         'Billing', 'payment methods, billing, cards, bank transfer'),

                                                        ('Do you provide 24/7 customer support?',
                                                         'We provide customer support during business hours (8 AM - 6 PM, Sunday to Thursday) with email support available 24/7. For critical issues, we have an emergency contact system for immediate assistance.',
                                                         'Support', 'customer support, business hours, emergency contact'),

                                                        ('Can you develop custom solutions for my business?',
                                                         'Yes, both Codeline and Rihal specialize in custom software development. We work closely with clients to understand their requirements and develop tailored solutions that meet their specific business needs and objectives.',
                                                         'Services', 'custom development, business solutions, requirements, tailored software');